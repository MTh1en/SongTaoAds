package com.capstone.ads.model;

import com.capstone.ads.model.enums.PaymentMethod;
import com.capstone.ads.model.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
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
    Integer amount;
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

    @ManyToOne
    CustomDesignRequests customDesignRequests;

    @AssertTrue(message = "Only one of order or customDesignRequest must be non-null")
    private boolean isValidRelation() {
        return (orders == null && customDesignRequests != null) || (orders != null && customDesignRequests == null);
    }
}
