package com.capstone.ads.scheduler;

import com.capstone.ads.repository.internal.NotificationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationScheduler {
    NotificationRepository notificationRepository;

    @Scheduled(fixedRate = 604800000)
    @Transactional
    public void deleteOldNotifications() {
        notificationRepository.deleteByCreatedAtBefore(LocalDateTime.now().minusDays(30));
    }
}
