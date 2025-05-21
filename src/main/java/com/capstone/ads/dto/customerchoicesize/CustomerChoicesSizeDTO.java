package com.capstone.ads.dto.customerchoicesize;

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
    Double sizeValue;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String customerChoicesId;
    String sizeId;
}
