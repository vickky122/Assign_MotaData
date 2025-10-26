package com.example.windows_client.service;


import com.example.windows_client.config.ClientConfig;
import com.example.windows_client.model.LogMessage;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.net.InetAddress;
import java.util.Random;
import java.util.concurrent.*;

@Component
public class LogGeneratorService {

    private final ClientConfig config;
    private final TcpUdpSenderService sender;
    private final MetricsService metricsService;
    private final ScheduledExecutorService scheduler;
    private final Random random = new Random();

    public LogGeneratorService(ClientConfig config,
                               TcpUdpSenderService sender,
                               MetricsService metricsService) {
        this.config = config;
        this.sender = sender;
        this.metricsService = metricsService;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setName("log-generator");
            t.setDaemon(true);
            return t;
        });
    }

    @PostConstruct
    public void start() {
        scheduleNext();
    }

    private void scheduleNext() {
        int delay = config.getMinIntervalMs() +
                random.nextInt(Math.max(1, config.getMaxIntervalMs() - config.getMinIntervalMs() + 1));
        scheduler.schedule(this::generateAndSend, delay, TimeUnit.MILLISECONDS);
    }

    private void generateAndSend() {
        try {
            LogMessage log = randomLog();
            sender.send(log);
            metricsService.incrementTotal();
            metricsService.incrementByCategory(log.getEventCategory());
            metricsService.incrementBySeverity(log.getSeverity());
        } finally {
            scheduleNext();
        }
    }

    private LogMessage randomLog() {
        String[] users = {"Admin", "Guest", "motadata", "svc_user", "john.doe"};
        String[] severities = {"INFO", "WARN", "ERROR"};
        String[] services = {"windows_login", "windows_logout", "file_audit"};
        String[] categories = {"login.audit", "logout.audit", "file.audit"};

        String username = users[random.nextInt(users.length)];
        String severity = severities[random.nextInt(severities.length)];
        int idx = random.nextInt(services.length);
        String service = services[idx];
        String category = categories[Math.min(idx, categories.length - 1)];
        String hostname = localHostname();
        String msg = buildMessage(service, username, severity);

        LogMessage lm = new LogMessage();
        lm.setService(service);
        lm.setEventCategory(category);
        lm.setUsername(username);
        lm.setHostname(hostname);
        lm.setSeverity(severity);
        lm.setMessage(msg);
        lm.setSourceIp("127.0.0.1");
        return lm;
    }

    private String localHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "windows-host";
        }
    }

    private String buildMessage(String service, String username, String severity) {
        if ("windows_login".equals(service)) {
            return "Microsoft-Windows-Security-Auditing: A user account was successfully logged on. Account Name: " + username;
        } else if ("windows_logout".equals(service)) {
            return "Microsoft-Windows-Security-Auditing: A user account was logged off. Account Name: " + username;
        } else {
            return "Microsoft-Windows-Security-Auditing: File access by " + username + " severity=" + severity;
        }
    }

    @PreDestroy
    public void stop() {
        scheduler.shutdown();
        try {
            scheduler.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
