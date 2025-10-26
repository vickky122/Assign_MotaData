package com.example.log_collector.service;

import com.example.log_collector.model.ParsedLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ForwardingService {

    private final WebClient webClient;

    // Central log server URL from application.properties
    public ForwardingService(@Value("${logserver.url}") String logServerUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(logServerUrl)
                .build();
    }

    public void forwardLog(ParsedLog log) {
        webClient.post()
                .uri("/ingest")
                .bodyValue(log)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(e -> System.err.println("Failed to forward log: " + e.getMessage()))
                .subscribe();
    }
}
