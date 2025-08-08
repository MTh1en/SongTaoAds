package com.capstone.ads.dto.ChatBotTopic;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatBotTopicResponse {
    String id;
    String modelChatBotId;
    String topicId;
    LocalDateTime createdAt;
}
