package com.log_server.log_server.controller;

import com.log_server.log_server.model.LogEntity;
import com.log_server.log_server.service.LogStorageService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/logs")
public class IngestController {

    private final LogStorageService logStorageService;

    public IngestController(LogStorageService logStorageService) {
        this.logStorageService = logStorageService;
    }

    @PostMapping("/ingest")
    public Mono<Void> ingest(@RequestBody String logJson) {
        return logStorageService.saveLog(logJson);
    }

    @GetMapping
    public Flux<LogEntity> getLogs(
            @RequestParam Optional<String> service,
            @RequestParam Optional<String> level,
            @RequestParam Optional<String> username,
            @RequestParam Optional<Boolean> isBlacklisted,
            @RequestParam Optional<Integer> limit
    ) {
        return logStorageService.getLogs(service, level, username, isBlacklisted, limit);
    }
}
