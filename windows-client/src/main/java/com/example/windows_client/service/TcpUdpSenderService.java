package com.example.windows_client.service;


import com.example.windows_client.config.ClientConfig;
import com.example.windows_client.model.LogMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

@Service
public class TcpUdpSenderService {

    private final ClientConfig config;
    private final ObjectMapper mapper = new ObjectMapper();

    public TcpUdpSenderService(ClientConfig config) {
        this.config = config;
    }

    public void send(LogMessage log) {
        String json;
        try {
            json = mapper.writeValueAsString(log);
        } catch (JsonProcessingException e) {
            return;
        }

        if ("tcp".equalsIgnoreCase(config.getCollectorProtocol())) {
            sendTcp(json);
        } else {
            sendUdp(json);
        }
    }

    private void sendUdp(String message) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            byte[] data = message.getBytes(StandardCharsets.UTF_8);
            InetAddress address = InetAddress.getByName(config.getCollectorHost());
            DatagramPacket packet = new DatagramPacket(data, data.length, address, config.getCollectorPort());
            socket.send(packet);
        } catch (Exception ignored) {
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    private void sendTcp(String message) {
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(config.getCollectorHost(), config.getCollectorPort()), 2000);
            OutputStream os = socket.getOutputStream();
            os.write(message.getBytes(StandardCharsets.UTF_8));
            os.write('\n');
            os.flush();
        } catch (Exception ignored) {
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
