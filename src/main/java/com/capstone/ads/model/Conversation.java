package com.capstone.ads.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.catalina.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    List<String> priceQuote;

    @OneToOne
    Topic topic;

    @ManyToOne
    Users user;

    @ManyToOne
    ModelChatBot modelChatBot;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime createdAt;

    @CreationTimestamp
    @Column(updatable = true)
    LocalDateTime updatedAt;
}
