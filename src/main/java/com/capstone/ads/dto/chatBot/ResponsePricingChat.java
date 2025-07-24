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
public class ResponsePricingChat {
    @JsonProperty("id")
    String id;

    @JsonProperty("object")
    String object;

    @JsonProperty("created")
    long created;

    @JsonProperty("model")
    String model;

    @JsonProperty("choices")
    List<Choice> choices;

    @JsonProperty("usage")
    Usage usage;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Choice {
        @JsonProperty("index")
        int index;

        @JsonProperty("message")
        Message message;

        @JsonProperty("finish_reason")
        String finishReason; // Changed to camelCase for Java convention, mapped to JSON's finish_reason
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Message {
        @JsonProperty("role")
        String role;

        @JsonProperty("content")
        String content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Usage {
        @JsonProperty("prompt_tokens")
        int promptTokens; // Changed to camelCase, mapped to JSON's prompt_tokens

        @JsonProperty("completion_tokens")
        int completionTokens; // Changed to camelCase, mapped to JSON's completion_tokens

        @JsonProperty("total_tokens")
        int totalTokens; // Changed to camelCase, mapped to JSON's total_tokens
    }
}