package com.capstone.ads.dto.conversation;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationDTO {
         String id;
         List<String> priceQuote;
         String topicId;
         String userId;
         String modelChatBotId;
         LocalDateTime createdAt;
         LocalDateTime updatedAt;
}
