package com.capstone.ads.dto.payment;

import com.capstone.ads.dto.CoreDTO;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestDTO;
import com.capstone.ads.dto.edited_design.EditedDesignDTO;
import com.capstone.ads.model.enums.PaymentMethod;
import com.capstone.ads.model.enums.PaymentStatus;
import com.capstone.ads.model.enums.PaymentType;
import com.capstone.ads.model.json.CustomerChoiceHistories;
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
}
