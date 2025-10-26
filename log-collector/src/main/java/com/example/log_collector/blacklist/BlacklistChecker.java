package com.example.log_collector.blacklist;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class BlacklistChecker {
   
    private final Set<String> blacklistedUsers = Set.of("root", "admin", "svc_user");
    private final Set<String> blacklistedIps = Set.of("192.168.0.10", "10.0.0.5");

    public boolean isBlacklisted(String username, String ip) {
        return (username != null && blacklistedUsers.contains(username)) ||
                (ip != null && blacklistedIps.contains(ip));
    }
}
