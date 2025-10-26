package com.example.windows_client.controller;

import com.example.windows_client.service.MetricsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MetricsController {

    private final MetricsService metrics;

    public MetricsController(MetricsService metrics) {
        this.metrics = metrics;
    }

    @GetMapping("/metrics")
    public Map<String, Object> metrics() {
        return Map.of(
                "total_generated", metrics.getTotal(),
                "by_category", metrics.getByCategory(),
                "by_severity", metrics.getBySeverity()
        );
    }
}
