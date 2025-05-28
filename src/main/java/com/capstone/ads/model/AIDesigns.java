package com.capstone.ads.model;

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
public class AIDesigns {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String image;
    String customerNote;

    @CreationTimestamp
    LocalDateTime creationAt;
    @UpdateTimestamp
    LocalDateTime updateAt;
    @ManyToOne
    CustomerDetail customerDetail;
}
