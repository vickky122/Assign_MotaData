package com.log_server.log_server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.log_server.log_server.model.LogEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class QueryService {
    private static final Path LOG_FILE = Paths.get("logs.jsonl");
    private final ObjectMapper mapper = new ObjectMapper();

    public Flux<LogEntity> queryLogs(String service, String level, String username,
                                     Boolean isBlacklisted, Integer limit) {
        if (!Files.exists(LOG_FILE)) return Flux.empty();

        try (Stream<String> lines = Files.lines(LOG_FILE)) {
            List<LogEntity> logs = lines
                    .map(this::toEntitySafe)
                    .filter(Objects::nonNull)
                    .filter(log -> filter(log, service, level, username, isBlacklisted))
                    .sorted(Comparator.comparingLong(log -> parseTimestamp(log.getTimestamp())))
                    .collect(Collectors.toList());

            if (limit != null && limit > 0 && limit < logs.size()) {
                logs = logs.subList(0, limit);
            }

            return Flux.fromIterable(logs);
        } catch (IOException e) {
            return Flux.error(new RuntimeException("Failed to query logs", e));
        }
    }

    private boolean filter(LogEntity log, String service, String level, String username, Boolean isBlacklisted) {
        if (service != null && (log.getEventCategory() == null || !log.getEventCategory().equalsIgnoreCase(service))) return false;
        if (level != null && (log.getSeverity() == null || !log.getSeverity().equalsIgnoreCase(level))) return false;
        if (username != null && (log.getUsername() == null || !log.getUsername().equalsIgnoreCase(username))) return false;
        if (isBlacklisted != null && log.isBlacklisted() != isBlacklisted) return false;
        return true;
    }

    private LogEntity toEntitySafe(String line) {
        try {
            return mapper.readValue(line, LogEntity.class);
        } catch (Exception e) {
            return null;
        }
    }

    private long parseTimestamp(String timestamp) {
        if (timestamp == null) return 0L;
        try {
            return java.time.Instant.parse(timestamp).toEpochMilli();
        } catch (Exception e) {
            try {
                return Long.parseLong(timestamp);
            } catch (NumberFormatException ex) {
                return 0L;
            }
        }
    }
}
