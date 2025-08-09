package com.capstone.ads.model;

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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String type; // ROLE or USER

    @ManyToOne
    Roles roleTarget;

    @ManyToOne
    Users userTarget;

    @Column(nullable = false, columnDefinition = "TEXT")
    String message;

    @CreationTimestamp
    LocalDateTime createdAt;
}