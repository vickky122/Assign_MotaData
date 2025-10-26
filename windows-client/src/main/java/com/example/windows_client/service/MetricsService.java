package com.example.windows_client.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MetricsService {

    private final AtomicLong total = new AtomicLong(0);
    private final ConcurrentHashMap<String, AtomicLong> byCategory = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> bySeverity = new ConcurrentHashMap<>();

    public void incrementTotal() {
        total.incrementAndGet();
    }

    public void incrementByCategory(String category) {
        byCategory.computeIfAbsent(category == null ? "unknown" : category, k -> new AtomicLong()).incrementAndGet();
    }

    public void incrementBySeverity(String severity) {
        bySeverity.computeIfAbsent(severity == null ? "UNKNOWN" : severity, k -> new AtomicLong()).incrementAndGet();
    }

    public long getTotal() {
        return total.get();
    }

    public Map<String, Long> getByCategory() {
        ConcurrentHashMap<String, Long> out = new ConcurrentHashMap<>();
        byCategory.forEach((k, v) -> out.put(k, v.get()));
        return out;
    }

    public Map<String, Long> getBySeverity() {
        ConcurrentHashMap<String, Long> out = new ConcurrentHashMap<>();
        bySeverity.forEach((k, v) -> out.put(k, v.get()));
        return out;
    }
}
