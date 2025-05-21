package com.capstone.ads.dto.order;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderConfirmRequest {
    private Timestamp deliveryDate;
}