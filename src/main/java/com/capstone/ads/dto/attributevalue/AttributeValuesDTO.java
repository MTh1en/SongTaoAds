package com.capstone.ads.dto.attributevalue;

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
    Double materialPrice;
    Double unitPrice;
    Boolean isAvailable;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    String attributesId;
}
