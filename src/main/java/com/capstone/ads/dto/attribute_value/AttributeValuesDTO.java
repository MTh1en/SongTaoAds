package com.capstone.ads.dto.attribute_value;

import com.capstone.ads.dto.CoreDTO;
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
    CoreDTO attributesId;
}
