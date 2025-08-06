package com.capstone.ads.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
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
public class ModelChatBot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String modelName;
    String previousModelName;
    boolean active;

    @CreationTimestamp
    LocalDateTime createdAt;

    @OneToMany(mappedBy = "modelChatBot")
    List<Topic> topics;
}
