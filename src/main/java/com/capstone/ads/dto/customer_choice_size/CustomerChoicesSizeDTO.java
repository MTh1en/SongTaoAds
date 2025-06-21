package com.capstone.ads.dto.customer_choice_size;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerChoicesSizeDTO {
    String id;
    Long sizeValue;
    Long maxSize;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String customerChoicesId;
    String sizeId;
}
