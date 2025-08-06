package com.capstone.ads.dto.topic;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopicDTO {
     String id;
     String title;
     String description;
     int maxQuestion;
     LocalDateTime createdAt;
     LocalDateTime updatedAt;
}
