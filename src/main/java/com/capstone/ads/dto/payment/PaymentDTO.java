package com.capstone.ads.dto.payment;

import com.capstone.ads.dto.CoreDTO;
import com.capstone.ads.model.enums.PaymentMethod;
import com.capstone.ads.model.enums.PaymentStatus;
import com.capstone.ads.model.enums.PaymentType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDTO {
    String id;
    Long amount;
    Long code;
    PaymentMethod method;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    PaymentStatus status;
    PaymentType type;
    CoreDTO orders;
}
