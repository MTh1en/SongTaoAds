package com.capstone.ads.dto.user_response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDTO {
     String id;
     String answer;
     String questionId; 
     String conversationId; 
     LocalDateTime createdAt;
     LocalDateTime updatedAt;
}
