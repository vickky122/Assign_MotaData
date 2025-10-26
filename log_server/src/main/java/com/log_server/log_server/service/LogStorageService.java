package com.log_server.log_server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.log_server.log_server.model.LogEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class LogStorageService {

    private static final Path LOG_FILE = Paths.get("logs.jsonl");
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Mono<Void> saveLog(String logJson) {
        return Mono.fromRunnable(() -> {
            try {
                // Ensure parent directory exists
                Path parentDir = LOG_FILE.getParent();
                if (parentDir != null && !Files.exists(parentDir)) {
                    Files.createDirectories(parentDir);
                }

                Files.writeString(LOG_FILE, logJson + System.lineSeparator(),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write log", e);
            }
        });
    }


    public Flux<LogEntity> getLogs(Optional<String> service, Optional<String> level,
                                   Optional<String> username, Optional<Boolean> isBlacklisted,
                                   Optional<Integer> limit) {
        return Flux.using(
                () -> Files.lines(LOG_FILE),
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
}
