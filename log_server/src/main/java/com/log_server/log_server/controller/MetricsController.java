package com.log_server.log_server.controller;


import com.log_server.log_server.service.MetricsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/metrics")
    public Mono<Map<String, Object>> metrics() {
        return metricsService.getMetrics();
    }
}
