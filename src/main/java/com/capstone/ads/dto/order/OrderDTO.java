package com.capstone.ads.dto.order;

import com.capstone.ads.dto.CoreDTO;
import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.model.enums.OrderType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {
    String id;
    String orderCode;
    String address;
    long totalConstructionAmount;
    long depositConstructionAmount;
    long remainingConstructionAmount;
    long totalDesignAmount;
    long depositDesignAmount;
    long remainingDesignAmount;
    long totalOrderAmount;
    long totalOrderDepositAmount;
    long totalOrderRemainingAmount;
    LocalDate estimatedDeliveryDate;
    String note;
    OrderType orderType;
    OrderStatus status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    CoreDTO contractors;
    UserDTO users;
}
