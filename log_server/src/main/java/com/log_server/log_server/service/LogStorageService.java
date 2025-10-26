package com.log_server.log_server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Stream;
import reactor.core.publisher.Flux;
import com.log_server.log_server.model.LogEntity;

@Service
public class LogStorageService {

    private static final Path LOG_FILE = Paths.get("logs.jsonl");
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Save incoming JSON (compact it to a single line) -> write on boundedElastic
    public Mono<Void> saveLog(String logJson) {
        return Mono.fromRunnable(() -> {
            try {
                // Compact JSON to one line (handle pretty or one-line input)
                String compact;
                try {
                    compact = objectMapper.writeValueAsString(objectMapper.readTree(logJson));
                } catch (Exception ex) {
                    // Fallback: if invalid JSON, just flatten line breaks
                    compact = logJson.replaceAll("\\r?\\n", " ");
                }

                // Ensure parent directory exists
                Path parent = LOG_FILE.getParent();
                if (parent != null && !Files.exists(parent)) {
                    Files.createDirectories(parent);
                }

                Files.writeString(LOG_FILE, compact + System.lineSeparator(),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException("Failed to write log", e);
            }
        }).subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic()).then();
    }


    // existing getLogs (you already have this) - no change required here
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
