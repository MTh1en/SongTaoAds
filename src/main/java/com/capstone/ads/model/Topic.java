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
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String title;
    String description;
    int maxQuestion;
    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime createdAt;

    @CreationTimestamp
    @Column(updatable = true)
    LocalDateTime updatedAt;
}
