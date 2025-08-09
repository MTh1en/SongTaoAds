package com.capstone.ads.controller;

import com.capstone.ads.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationController {
    NotificationService notificationService;

    @PostMapping("/role/{role}")
    public ResponseEntity<String> sendRoleNotification(
            @PathVariable String role,
            @RequestBody String message
    ) {
        if (role == null || role.trim().isEmpty() || message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Role and message cannot be empty");
        }
        try {
            notificationService.sendNotificationToRole(role, message);
            return ResponseEntity.ok("Notification sent to role: " + role);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send notification: " + e.getMessage());
        }
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<String> sendUserNotification(
            @PathVariable String userId,
            @RequestBody String message
    ) {
        if (userId == null || userId.trim().isEmpty() || message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("UserId and message cannot be empty");
        }
        try {
            notificationService.sendNotificationToUser(userId, message);
            return ResponseEntity.ok("Notification sent to user: " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send notification: " + e.getMessage());
        }
    }
}
