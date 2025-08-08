package com.capstone.ads.model;

import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class ChatBotLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String question;
    @Column(length = 1000)
    String answer;

    @ManyToOne
    ModelChatBot modelChatBot;
    @CreationTimestamp
    LocalDateTime createdAt;

    @ManyToOne
    Users users;
}
