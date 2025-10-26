package com.example.log_collector.service;

import com.example.log_collector.blacklist.BlacklistChecker;
import com.example.log_collector.config.CollectorConfig;
import com.example.log_collector.metrics.MetricsService;
import com.example.log_collector.model.ParsedLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

@Service
public class TcpUdpListenerService {

    private final CollectorConfig config;
    private final MetricsService metrics;
    private final BlacklistChecker blacklistChecker;
    private final LogParserService parserService;
    private final ForwardingService forwardingService;
    private final ExecutorService workerPool;
    private final BlockingQueue<String> logQueue;
    private final ObjectMapper mapper = new ObjectMapper();

    private volatile boolean running = true;

    public TcpUdpListenerService(CollectorConfig config,
                                 MetricsService metrics,
                                 BlacklistChecker blacklistChecker,
                                 LogParserService parserService,
                                 ForwardingService forwardingService) {
        this.config = config;
        this.metrics = metrics;
        this.blacklistChecker = blacklistChecker;
        this.parserService = parserService;
        this.forwardingService = forwardingService;

        this.logQueue = new LinkedBlockingQueue<>(config.getMaxQueueSize());
        this.workerPool = Executors.newFixedThreadPool(4); // limited worker threads
    }

    @PostConstruct
    public void start() {
        if ("udp".equalsIgnoreCase(config.getProtocol())) {
            startUdpListener();
        } else {
            startTcpListener();
        }

        // Start worker threads to process queued logs
        for (int i = 0; i < 4; i++) {
            workerPool.submit(this::processLogs);
        }
    }

    private void startTcpListener() {
        Thread tcpThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(config.getTcpPort())) {
                while (running) {
                    Socket client = serverSocket.accept();
                    workerPool.submit(() -> handleTcpClient(client));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        tcpThread.setDaemon(true);
        tcpThread.start();
    }

    private void handleTcpClient(Socket client) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                enqueueLog(line);
            }
        } catch (Exception ignored) {}
    }

    private void startUdpListener() {
        Thread udpThread = new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(config.getUdpPort())) {
                byte[] buffer = new byte[65535];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                while (running) {
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());
                    enqueueLog(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        udpThread.setDaemon(true);
        udpThread.start();
    }

    private void enqueueLog(String log) {
        if (!logQueue.offer(log)) {
            // Queue full, discard or handle backpressure
            System.out.println("Queue full, dropping log: " + log);
        }
    }

    private void processLogs() {
        while (running) {
            try {
                String rawLog = logQueue.take();
                ParsedLog parsedLog = parserService.parse(rawLog);

                boolean blacklisted = blacklistChecker.isBlacklisted(parsedLog.getUsername(), "127.0.0.1");
                parsedLog.setBlacklisted(blacklisted);

                metrics.incrementTotal();
                metrics.incrementByCategory(parsedLog.getEventCategory());
                metrics.incrementBySeverity(parsedLog.getSeverity());
                if (blacklisted) metrics.incrementBlacklisted();

                forwardingService.forwardLog(parsedLog);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception ignored) {}
        }
    }

    @PreDestroy
    public void stop() {
        running = false;
        workerPool.shutdown();
        try {
            workerPool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
