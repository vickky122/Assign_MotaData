package com.log_server.log_server.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.Test;
import reactor.test.StepVerifier;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LogStorageServiceTest {

    static Path tmp = Paths.get("target/test-logs.jsonl");

    @BeforeEach
    public void cleanup() throws Exception {
        Files.deleteIfExists(tmp);
    }

    @AfterEach
    public void cleanup2() throws Exception {
        Files.deleteIfExists(tmp);
    }

    @Test
    public void saveAndReadBack() {
        LogStorageService svc = new LogStorageService(tmp.toString());
        String json = "{\"eventCategory\":\"login.au    dit\",\"severity\":\"INFO\",\"username\":\"v\",\"blacklisted\":false,\"message\":\"ok\",\"timestamp\":\"2025-10-26T12:00:00Z\"}";

        StepVerifier.create(svc.saveLog(json))
                .verifyComplete();

        StepVerifier.create(svc.getLogs(java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty()))
                .expectNextMatches(e -> e.getUsername().equals("v") && e.getEventCategory().equals("login.audit"))
                .verifyComplete();
    }
}
