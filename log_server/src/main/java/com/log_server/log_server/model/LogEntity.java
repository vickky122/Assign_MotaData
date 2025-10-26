package com.log_server.log_server.model;

public class LogEntity {
    private String eventCategory;
    private String severity;
    private String username;
    private boolean blacklisted;
    private String message;
    private String timestamp;

    public String getEventCategory() { return eventCategory; }
    public void setEventCategory(String eventCategory) { this.eventCategory = eventCategory; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public boolean isBlacklisted() { return blacklisted; }
    public void setBlacklisted(boolean blacklisted) { this.blacklisted = blacklisted; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
