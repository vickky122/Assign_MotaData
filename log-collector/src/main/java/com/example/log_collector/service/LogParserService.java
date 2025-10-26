package com.example.log_collector.service;

import com.example.log_collector.model.ParsedLog;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class LogParserService {

    private final ObjectMapper mapper = new ObjectMapper();

    public ParsedLog parse(String rawJson) {
        try {
            JsonNode node = mapper.readTree(rawJson);

            String timestamp = node.has("timestamp") ? node.get("timestamp").asText() : Instant.now().toString();
            String username = node.has("username") ? node.get("username").asText() : "unknown";
            String hostname = node.has("hostname") ? node.get("hostname").asText() : "unknown";
            String severity = node.has("severity") ? node.get("severity").asText() : "INFO";
            String service = node.has("service") ? node.get("service").asText() : "unknown";

            // Map service to category
            String eventCategory = mapServiceToCategory(service);

            String rawMessage = node.has("message") ? node.get("message").asText() : rawJson;

            return new ParsedLog(timestamp, eventCategory, username, hostname, severity, rawMessage);

        } catch (Exception e) {
            // fallback in case of parsing error
            return new ParsedLog(Instant.now().toString(), "unknown", "unknown",
                    "unknown", "INFO", rawJson);
        }
    }

    private String mapServiceToCategory(String service) {
        switch (service) {
            case "windows_login": return "login.audit";
            case "windows_logout": return "logout.audit";
            case "file_audit": return "file.audit";
            default: return "unknown";
        }
    }
}
