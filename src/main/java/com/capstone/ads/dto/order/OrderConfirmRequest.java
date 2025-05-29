package com.capstone.ads.dto.order;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderConfirmRequest {
    private LocalDateTime deliveryDate;
}