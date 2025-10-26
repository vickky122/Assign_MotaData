package com.example.log_collector.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "collector")
public class CollectorConfig {

    private int tcpPort = 9000;
    private int udpPort = 9001;
    private int maxQueueSize = 1000;
    private String centralServerUrl = "http://localhost:9100/ingest";
    private String protocol = "tcp"; // tcp or udp

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    public void setMaxQueueSize(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }

    public String getCentralServerUrl() {
        return centralServerUrl;
    }

    public void setCentralServerUrl(String centralServerUrl) {
        this.centralServerUrl = centralServerUrl;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
