package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Notification;
import com.capstone.ads.model.NotificationStatus;
import com.capstone.ads.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationStatusRepository extends JpaRepository<NotificationStatus, Long> {
    Optional<NotificationStatus> findByNotificationAndUsers(Notification notification, Users user);

    Page<NotificationStatus> findByUsersAndIsReadAndNotification_TypeOrderByNotification_CreatedAtDesc(
            Users users, boolean isRead, String type, Pageable pageable
    );

    Page<NotificationStatus> findByUsersAndNotification_TypeOrderByNotification_CreatedAtDesc(
            Users users, String type, Pageable pageable
    );
}