package com.capstone.ads.model;

import com.capstone.ads.model.enums.CustomDesignStatus;
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
public class CustomDesigns {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String image;
    String customerNote;
    String designerDescription;

    @CreationTimestamp
    LocalDateTime createAt;
    @UpdateTimestamp
    LocalDateTime updateAt;

    @Enumerated(EnumType.STRING)
    CustomDesignStatus status;

    @ManyToOne
    CustomDesignRequests customDesignRequests;
}
