package com.capstone.ads.dto.model_chat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModelChatBotDTO {
    String id;
    String modelName;
    String previousModelName;
    boolean active;
    LocalDateTime createdAt;
}
