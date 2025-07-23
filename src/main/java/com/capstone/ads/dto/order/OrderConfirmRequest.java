package com.capstone.ads.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderConfirmRequest {
    LocalDateTime estimatedDeliveryDate;
    String contractorId;
}