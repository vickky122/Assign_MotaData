package com.log_server.log_server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.log_server.log_server.model.LogEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MetricsService {

    private static final Path LOG_FILE = Paths.get("logs.jsonl");
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Mono<Map<String, Object>> getMetrics() {
        return Mono.fromSupplier(() -> {
            Map<String, Object> metrics = new HashMap<>();
            List<LogEntity> logs = new ArrayList<>();

            if (!Files.exists(LOG_FILE)) {
                metrics.put("total_generated", 0);
                metrics.put("by_category", Map.of());
                metrics.put("by_severity", Map.of());
                return metrics;
            }

            try (var lines = Files.lines(LOG_FILE)) {
                logs = lines.map(line -> {
                    try {
                        return objectMapper.readValue(line, LogEntity.class);
                    } catch (Exception e) {
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            metrics.put("total_generated", logs.size());
            metrics.put("by_category", logs.stream()
                    .collect(Collectors.groupingBy(LogEntity::getEventCategory, Collectors.counting())));
            metrics.put("by_severity", logs.stream()
                    .collect(Collectors.groupingBy(LogEntity::getSeverity, Collectors.counting())));

            return metrics;
        });
    }
}
