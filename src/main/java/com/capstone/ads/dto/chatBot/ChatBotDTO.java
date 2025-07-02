package com.capstone.ads.dto.chatBot;

import com.capstone.ads.model.ChatBotLog;
import com.capstone.ads.model.Users;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatBotDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    Users users;

    @ManyToOne
    ChatBotLog chatBotLog;

    String question;
    String answer;

    @CreationTimestamp
    LocalDateTime createdAt;
}
