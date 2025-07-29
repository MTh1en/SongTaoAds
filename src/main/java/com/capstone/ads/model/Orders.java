package com.capstone.ads.model;

import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.model.enums.OrderType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String orderCode;
    String address;
    Long totalConstructionAmount;
    Long depositConstructionAmount;
    Long remainingConstructionAmount;
    Long totalDesignAmount;
    Long depositDesignAmount;
    Long remainingDesignAmount;
    Long totalOrderAmount;
    Long totalOrderDepositAmount;
    Long totalOrderRemainingAmount;
    String note;
    Boolean isPaymentCompleted;

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    OrderType orderType;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    @ManyToOne
    Users users;

    @ManyToOne
    Contractors contractors;

    @OneToMany(mappedBy = "orders")
    List<Payments> payments;

    @OneToMany(mappedBy = "orders")
    List<Feedbacks> feedbacks;

    @OneToMany(mappedBy = "orders")
    List<ProgressLogs> progressLogs;

    @OneToOne(mappedBy = "orders", cascade = CascadeType.ALL)
    Contract contract;

    @OneToMany(mappedBy = "orders")
    List<OrderDetails> orderDetails;
}
