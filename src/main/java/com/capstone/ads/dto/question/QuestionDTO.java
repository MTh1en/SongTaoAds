package com.capstone.ads.dto.question;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionDTO {
    String id;
    String question;
    int number;
    String topicId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
