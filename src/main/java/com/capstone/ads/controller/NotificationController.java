package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.notification.NotificationDTO;
import com.capstone.ads.service.NotificationService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationController {
    NotificationService notificationService;

    @PostMapping("/roles/{role}")
    public ApiResponse<Void> sendRoleNotification(
            @PathVariable String role,
            @RequestBody String message
    ) {
        notificationService.sendNotificationToRole(role, message);
        return ApiResponseBuilder.buildSuccessResponse("Gửi thông báo đến ROLE: " + role, null);
    }

    @PostMapping("/users/{userId}")
    public ApiResponse<Void> sendUserNotification(
            @PathVariable String userId,
            @RequestBody String message
    ) {
        notificationService.sendNotificationToUser(userId, message);
        return ApiResponseBuilder.buildSuccessResponse("NGửi thông báo đến USER: " + userId, null);
    }

    @GetMapping(value = "/roles", params = {"isRead"})
    public ApiPagingResponse<NotificationDTO> getRoleNotifications(
            @RequestParam(defaultValue = "false") boolean isRead,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        var response = notificationService.getRoleNotificationsForUser(isRead, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse(
                "Xem thông báo đọc/ chưa đọc theo role",
                response, page
        );
    }

    @GetMapping(value = "/roles", params = {"!isRead"})
    public ApiPagingResponse<NotificationDTO> getAllRoleNotifications(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        var response = notificationService.getAllRoleNotificationsForUser(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse(
                "Xem tất cả thông báo theo role",
                response, page
        );
    }

    @GetMapping(value = "/users", params = {"isRead"})
    public ApiPagingResponse<NotificationDTO> getUserNotifications(
            @RequestParam(defaultValue = "false") boolean isRead,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        var response = notificationService.getUserNotificationsForUser(isRead, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse(
                "Xem thông báo đọc/chưa đọc theo tài khoản",
                response, page
        );
    }

    @GetMapping(value = "/users", params = {"!isRead"})
    public ApiPagingResponse<NotificationDTO> getAllUserNotifications(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        var response = notificationService.getAllUserNotificationsForUser(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse(
                "Xem tất cả thông báo theo tài khoản",
                response, page
        );
    }

    @PostMapping("/mark-read/{notificationId}")
    public ApiResponse<Void> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ApiResponseBuilder.buildSuccessResponse("Notification marked as read", null);
    }
}
