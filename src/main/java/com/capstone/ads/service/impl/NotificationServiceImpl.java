package com.capstone.ads.service.impl;

import com.capstone.ads.dto.notification.NotificationDTO;
import com.capstone.ads.dto.notification.NotificationEvent;
import com.capstone.ads.event.RoleNotificationEvent;
import com.capstone.ads.event.UserNotificationEvent;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.NotificationMapper;
import com.capstone.ads.model.Notification;
import com.capstone.ads.model.NotificationStatus;
import com.capstone.ads.model.Roles;
import com.capstone.ads.model.Users;
import com.capstone.ads.repository.internal.NotificationRepository;
import com.capstone.ads.repository.internal.NotificationStatusRepository;
import com.capstone.ads.repository.internal.RolesRepository;
import com.capstone.ads.service.NotificationService;
import com.capstone.ads.service.UserService;
import com.capstone.ads.utils.SecurityContextUtils;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl implements NotificationService {
    SocketIOServer socketIOServer;
    NotificationRepository notificationRepository;
    NotificationStatusRepository notificationStatusRepository;
    UserService userService;
    RolesRepository rolesRepository;
    SecurityContextUtils securityContextUtils;
    NotificationMapper notificationMapper;

    @Async
    @EventListener
    @Transactional
    public void handleRoleNotificationEvent(RoleNotificationEvent event) {
        sendNotificationToRole(event.getRoleName(), event.getMessage());
    }

    @Override
    @Transactional
    public void sendNotificationToRole(String roleName, String message) {
        Roles role = rolesRepository.findById(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        Notification notification = Notification.builder()
                .type("ROLE")
                .roleTarget(role)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);

        socketIOServer.getRoomOperations(roleName).sendEvent("role_notification",
                new NotificationEvent(
                        notification.getId(),
                        "ROLE", message,
                        notification.getCreatedAt().toString()));
    }

    @Async
    @EventListener
    @Transactional
    public void handleUserNotificationEvent(UserNotificationEvent event) {
        sendNotificationToUser(event.getUserId(), event.getMessage());
    }

    @Override
    @Transactional
    public void sendNotificationToUser(String userId, String message) {
        Users user = userService.getUserByIdAndIsActive(userId);

        Notification notification = Notification.builder()
                .type("USER")
                .userTarget(user)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();
        notification = notificationRepository.save(notification);

        NotificationStatus status = NotificationStatus.builder()
                .notification(notification)
                .users(user)
                .isRead(false)
                .build();
        notificationStatusRepository.save(status);

        socketIOServer.getRoomOperations(userId).sendEvent("user_notification",
                new NotificationEvent(
                        notification.getId(),
                        "USER", message,
                        notification.getCreatedAt().toString()));
    }

    @Override
    @Transactional
    public Page<NotificationDTO> getRoleNotificationsForUser(boolean isRead, int page, int size) {
        Users user = securityContextUtils.getCurrentUser();
        String roleName = user.getRoles().getName();

        // Tạo notification_status cho role-based notifications nếu chưa có
        createMissingNotificationStatus(notificationRepository.findByRoleTargetName(roleName), user);

        // Lấy thông báo role-based
        Pageable pageable = PageRequest.of(page - 1, size);

        return notificationStatusRepository.findByUsersAndIsReadAndNotification_TypeOrderByNotification_CreatedAtDesc(user, isRead, "ROLE", pageable)
                .map(notificationMapper::toDTO);
    }

    @Override
    @Transactional
    public Page<NotificationDTO> getAllRoleNotificationsForUser(int page, int size) {
        Users user = securityContextUtils.getCurrentUser();
        String roleName = user.getRoles().getName();

        // Tạo notification_status cho role-based notifications nếu chưa có
        createMissingNotificationStatus(notificationRepository.findByRoleTargetName(roleName), user);

        // Lấy thông báo role-based
        Pageable pageable = PageRequest.of(page - 1, size);
        return notificationStatusRepository.findByUsersAndNotification_TypeOrderByNotification_CreatedAtDesc(user, "ROLE", pageable)
                .map(notificationMapper::toDTO);
    }

    @Override
    @Transactional
    public Page<NotificationDTO> getUserNotificationsForUser(boolean isRead, int page, int size) {
        Users user = securityContextUtils.getCurrentUser();
        String userId = user.getId();

        // Tạo notification_status cho user-based notifications nếu chưa có
        createMissingNotificationStatus(notificationRepository.findByUserTarget_Id(userId), user);

        // Lấy thông báo user-based
        Pageable pageable = PageRequest.of(page - 1, size);

        return notificationStatusRepository.findByUsersAndIsReadAndNotification_TypeOrderByNotification_CreatedAtDesc(user, isRead, "USER", pageable)
                .map(notificationMapper::toDTO);
    }

    @Override
    @Transactional
    public Page<NotificationDTO> getAllUserNotificationsForUser(int page, int size) {
        Users user = securityContextUtils.getCurrentUser();
        String userId = user.getId();

        // Tạo notification_status cho user-based notifications nếu chưa có
        createMissingNotificationStatus(notificationRepository.findByUserTarget_Id(userId), user);

        // Lấy thông báo user-based
        Pageable pageable = PageRequest.of(page - 1, size);
        return notificationStatusRepository.findByUsersAndNotification_TypeOrderByNotification_CreatedAtDesc(user, "USER", pageable)
                .map(notificationMapper::toDTO);
    }

    @Override
    @Transactional
    public void markNotificationAsRead(Long notificationId) {
        Users user = securityContextUtils.getCurrentUser();
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));
        NotificationStatus status = notificationStatusRepository.findByNotificationAndUsers(notification, user)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));
        status.setIsRead(true);
        status.setReadAt(LocalDateTime.now());
        notificationStatusRepository.save(status);
    }

    //INTERNAL FUNCTION

    private void createMissingNotificationStatus(List<Notification> notifications, Users user) {
        List<NotificationStatus> newStatuses = notifications.stream()
                .filter(notification -> notificationStatusRepository.findByNotificationAndUsers(notification, user).isEmpty())
                .map(notification -> NotificationStatus.builder()
                        .notification(notification)
                        .users(user)
                        .isRead(false)
                        .build())
                .collect(Collectors.toList());
        if (!newStatuses.isEmpty()) {
            notificationStatusRepository.saveAll(newStatuses);
        }
    }
}
