package com.capstone.ads.dto.chatBot;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatCompletionRequest {
    String model; // Required: Model ID (e.g., "gpt-4.1")
    List<Message> messages; // Required: List of messages in the conversation

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        String role;     // "system", "user", "assistant"
        String content;  // The message content
    }
}
