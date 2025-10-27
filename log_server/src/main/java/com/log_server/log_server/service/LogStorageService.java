package com.log_server.log_server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.log_server.log_server.model.LogEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.nio.file.Paths;
import java.nio.file.Path;


@Service
public class LogStorageService {

    private Path logFile = Paths.get("target/logs.jsonl");
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Save incoming JSON log to file (one line per entry)
    public Mono<Void> saveLog(String logJson) {
        return Mono.fromRunnable(() -> {
            try {
                String compact;
                try {
                    compact = objectMapper.writeValueAsString(objectMapper.readTree(logJson));
                } catch (Exception ex) {
                    compact = logJson.replaceAll("\\r?\\n", " ");
                }

                Path parent = logFile.getParent();
                if (parent != null && !Files.exists(parent)) {
                    Files.createDirectories(parent);
                }

                Files.writeString(logFile, compact + System.lineSeparator(),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write log", e);
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    // Read logs with optional filters
    public Flux<LogEntity> getLogs(Optional<String> service, Optional<String> level,
                                   Optional<String> username, Optional<Boolean> isBlacklisted,
                                   Optional<Integer> limit) {
        if (!Files.exists(logFile)) {
            return Flux.empty();
        }

        return Flux.using(
                () -> Files.lines(logFile),
                lines -> Flux.fromStream(lines.map(line -> {
                                    try {
                                        return objectMapper.readValue(line, LogEntity.class);
                                    } catch (Exception e) {
                                        return null;
                                    }
                                }).filter(Objects::nonNull)
                                .filter(log -> service.map(s -> s.equalsIgnoreCase(log.getEventCategory())).orElse(true))
                                .filter(log -> level.map(l -> l.equalsIgnoreCase(log.getSeverity())).orElse(true))
                                .filter(log -> username.map(u -> u.equalsIgnoreCase(log.getUsername())).orElse(true))
                                .filter(log -> isBlacklisted.map(b -> b == log.isBlacklisted()).orElse(true))
                                .limit(limit.orElse(Integer.MAX_VALUE))
                ),
                Stream::close
        );
    }

    // Used for testing (custom file path)
    public LogStorageService(String customPath) {
        this.logFile = Paths.get(customPath);
    }

    // Default constructor for Spring
    public LogStorageService() {
    }
}
