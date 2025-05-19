package com.capstone.ads.dto.payment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentResponseDTO {
    private String paymentId;
    private String paymentUrl;
}