package com.capstone.ads.model;

import com.capstone.ads.model.enums.DemoDesignStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class DemoDesigns {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String designerDescription;
    String demoImage;

    String customerNote;
    String customerFeedbackImage;

    Integer version;

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    DemoDesignStatus status;

    @ManyToOne
    CustomDesignRequests customDesignRequests;
}
