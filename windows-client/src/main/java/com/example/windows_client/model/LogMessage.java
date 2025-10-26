package com.example.windows_client.model;

import java.time.Instant;

public class LogMessage {
    private String timestamp;
    private String service;
    private String eventCategory;
    private String username;
    private String hostname;
    private String severity;
    private String message;
    private String sourceIp;

    public LogMessage() {
        this.timestamp = Instant.now().toString();
    }

    public LogMessage(String service, String eventCategory, String username,
                      String hostname, String severity, String message, String sourceIp) {
        this.timestamp = Instant.now().toString();
        this.service = service;
        this.eventCategory = eventCategory;
        this.username = username;
        this.hostname = hostname;
        this.severity = severity;
        this.message = message;
        this.sourceIp = sourceIp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }
}
