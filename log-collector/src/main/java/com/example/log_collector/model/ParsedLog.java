package com.example.log_collector.model;

import java.time.Instant;

public class ParsedLog {

    private String timestamp;
    private String eventCategory;
    private String username;
    private String hostname;
    private String severity;
    private String rawMessage;
    private boolean isBlacklisted;

    public ParsedLog() {
        this.timestamp = Instant.now().toString();
    }

    public ParsedLog(String timestamp, String eventCategory, String username,
                     String hostname, String severity, String rawMessage) {
        this.timestamp = timestamp;
        this.eventCategory = eventCategory;
        this.username = username;
        this.hostname = hostname;
        this.severity = severity;
        this.rawMessage = rawMessage;
        this.isBlacklisted = false;
    }


    public String getTimestamp() {
        return timestamp;
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

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    public boolean isBlacklisted() {
        return isBlacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        isBlacklisted = blacklisted;
    }
}
