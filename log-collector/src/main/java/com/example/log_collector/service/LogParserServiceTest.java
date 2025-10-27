package com.example.log_collector.service;

import com.example.log_collector.model.ParsedLog;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogParserServiceTest {

    private final LogParserService parser = new LogParserService();

    @Test
    public void parseSimpleJson() {
        String json = "{\"timestamp\":\"2025-10-26T12:00:00Z\",\"service\":\"windows_login\",\"username\":\"root\",\"hostname\":\"host1\",\"severity\":\"INFO\",\"message\":\"ok\"}";
        ParsedLog p = parser.parse(json);
        assertNotNull(p);
        assertEquals("login.audit", p.getEventCategory());
        assertEquals("root", p.getUsername());
        assertEquals("INFO", p.getSeverity());
    }

    @Test
    public void parseInvalidJsonProducesFallback() {
        String bad = "this is not json";
        ParsedLog p = parser.parse(bad);
        assertNotNull(p);
        assertEquals("unknown", p.getEventCategory());
        assertEquals("unknown", p.getUsername());
    }
}
