package com.capstone.ads.controller;

import com.capstone.ads.service.AccessTokenService;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SocketHandler {
    SocketIOServer server;
    AccessTokenService accessTokenService;

    @OnConnect
    public void onConnect(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        if (accessTokenService.isTokenValid(token)) {
            String userId = accessTokenService.extractUserId(token);
            String role = accessTokenService.extractRole(token);
            client.set("userId", userId);
            client.set("role", role);

            client.joinRoom(role);
            client.joinRoom(userId);
            log.info("Client connected: {} to room: {}", userId, role);
        }
        log.info("Client session connected: {}", client.getSessionId());
    }

    @OnDisconnect
    public void clientDisconnected(SocketIOClient client) {
        log.info("Client disConnected: {}", client.getSessionId());
    }

    @PostConstruct
    public void startServer() {
        server.start();
        server.addListeners(this);
        log.info("Socket server started");
    }

    @PreDestroy
    public void stopServer() {
        server.stop();
        log.info("Socket server stoped");
    }
}
