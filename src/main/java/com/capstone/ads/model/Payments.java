package com.capstone.ads.model;

import com.capstone.ads.model.enums.PaymentMethod;
import com.capstone.ads.model.enums.PaymentStatus;
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
    Integer totalAmount;
    Long code;
    @Enumerated(EnumType.STRING)
    PaymentMethod method;

    @CreationTimestamp
    LocalDateTime createAt;
    @UpdateTimestamp
    LocalDateTime updateAt;

    @Enumerated(EnumType.STRING)
    PaymentStatus status;

    Boolean isDeposit;

    @ManyToOne
    Orders orders;
}
