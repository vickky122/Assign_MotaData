package com.example.windows_client.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Value("${collector.host:localhost}")
    private String collectorHost;

    @Value("${collector.port:5514}")
    private int collectorPort;

    @Value("${collector.protocol:udp}")
    private String collectorProtocol;

    @Value("${generator.min-interval-ms:1000}")
    private int minIntervalMs;

    @Value("${generator.max-interval-ms:2000}")
    private int maxIntervalMs;

    public String getCollectorHost() {
        return collectorHost;
    }

    public int getCollectorPort() {
        return collectorPort;
    }

    public String getCollectorProtocol() {
        return collectorProtocol;
    }

    public int getMinIntervalMs() {
        return minIntervalMs;
    }

    public int getMaxIntervalMs() {
        return maxIntervalMs;
    }
}
