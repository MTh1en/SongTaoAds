package com.capstone.ads.dto.attribute;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributesCreateRequest {
    String name;
    String calculateFormula;
    Boolean isAvailable;
    Boolean isCore;
}
