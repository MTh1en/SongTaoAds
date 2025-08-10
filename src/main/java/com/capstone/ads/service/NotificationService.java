package com.capstone.ads.service;

import com.capstone.ads.dto.notification.NotificationDTO;
import org.springframework.data.domain.Page;

public interface NotificationService {
    void sendNotificationToRole(String roleName, String message);

    void sendNotificationToUser(String userId, String message);

    Page<NotificationDTO> getRoleNotificationsForUser(boolean isRead, int page, int size);

    Page<NotificationDTO> getAllRoleNotificationsForUser(int page, int size);

    Page<NotificationDTO> getUserNotificationsForUser(boolean isRead, int page, int size);

    Page<NotificationDTO> getAllUserNotificationsForUser(int page, int size);

    void markNotificationAsRead(Long notificationId);
}
