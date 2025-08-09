package com.capstone.ads.service;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {
    SocketIOServer socketIOServer;

    public void sendNotificationToRole(String role, String message) {
        socketIOServer.getRoomOperations(role).sendEvent("role_notification", message);
    }

    public void sendNotificationToUser(String userId, String message) {
        socketIOServer.getRoomOperations(userId).sendEvent("user_notification", message);
    }
}
