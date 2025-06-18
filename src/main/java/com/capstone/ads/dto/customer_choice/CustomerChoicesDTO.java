package com.capstone.ads.dto.customer_choice;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerChoicesDTO {
    String id;
    Double totalAmount;
    Boolean isFinal;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String userId;
    String productTypeId;
}
