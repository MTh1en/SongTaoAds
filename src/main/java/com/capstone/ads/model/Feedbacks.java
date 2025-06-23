package com.capstone.ads.model;

import com.capstone.ads.model.enums.FeedbackStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Feedbacks {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    Integer rating;
    String comment;
    String feedbackImageUrl;
    LocalDateTime sendAt;

    String response;
    LocalDateTime responseAt;

    @Enumerated(EnumType.STRING)
    FeedbackStatus status;

    @ManyToOne
    Users sendBy;

    @ManyToOne
    Orders orders;
}
