package com.capstone.ads.dto.chatBot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestPricingChat {
    @JsonProperty("model")
    String model;

    @JsonProperty("messages")
    List<Message> messages;

    @JsonProperty("max_tokens")
    Integer maxTokens; // Optional, limits response length

    @JsonProperty("temperature")
    Double temperature;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Message {
        @JsonProperty("role")
        String role; // e.g., "system", "user", "assistant"

        @JsonProperty("content")
        String content;
    }
}
