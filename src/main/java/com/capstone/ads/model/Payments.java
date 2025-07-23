package com.capstone.ads.model;

import com.capstone.ads.model.enums.PaymentMethod;
import com.capstone.ads.model.enums.PaymentStatus;
import com.capstone.ads.model.enums.PaymentType;
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
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    Long amount;
    Long code;
    @Enumerated(EnumType.STRING)
    PaymentMethod method;

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    PaymentStatus status;

    @Enumerated(EnumType.STRING)
    PaymentType type;

    @ManyToOne
    Orders orders;
}
