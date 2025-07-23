package com.capstone.ads.model;

import com.capstone.ads.model.enums.OrderStatus;
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
public class ProgressLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String description;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    @CreationTimestamp
    LocalDateTime createdAt;
    String createdBy;

    @ManyToOne
    Orders orders;

    @OneToMany(mappedBy = "progressLogs")
    List<FileData> fileData;
}
