package com.capstone.ads.dto.attribute_value;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributeValuesDTO {
    String id;
    String name;
    String unit;
    Long materialPrice;
    Long unitPrice;
    Boolean isMultiplier;
    Boolean isAvailable;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String attributesId;
}
