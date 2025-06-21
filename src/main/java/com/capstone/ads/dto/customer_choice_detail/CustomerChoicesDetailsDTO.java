package com.capstone.ads.dto.customer_choice_detail;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerChoicesDetailsDTO {
    String id;
    Long subTotal;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String customerChoicesId;
    String attributeValuesId;
}

