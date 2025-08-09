package com.capstone.ads.dto.order;

import com.capstone.ads.model.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderTrackingResponse {
    String orderCode;
    OrderStatus status;
    LocalDate estimatedDeliveryDate;
    String message;
}
