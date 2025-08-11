package com.capstone.ads.dto.notification;

import com.capstone.ads.dto.CoreDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationDTO {
    Long notificationId;
    String type; // ROLE or USER
    CoreDTO roleTarget;
    CoreDTO userTarget;
    String message;
    Boolean isRead;
    LocalDateTime createdAt;
}
