package com.capstone.ads.dto.ChatBotTopic;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatBotTopicCreateRequest {
    String modelChatBotId;
    String topicId;
}
