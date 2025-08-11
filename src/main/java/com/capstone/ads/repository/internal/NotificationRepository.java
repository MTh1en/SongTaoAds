package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRoleTargetName(String roleName);

    List<Notification> findByUserTarget_Id(String id);

    void deleteByCreatedAtBefore(LocalDateTime createdAt);
}