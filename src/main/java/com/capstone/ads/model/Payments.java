package com.capstone.ads.model;

import com.capstone.ads.model.enums.PaymentMethod;
import com.capstone.ads.model.enums.PaymentStatus;
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
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    Double totalAmount;

    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @CreationTimestamp
    LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;

    Boolean isDeposit;

    @ManyToOne
    Orders orders;
    @Column
    private String payOsPaymentLinkId;
}
