package com.capstone.ads.dto.cost_type;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CostTypeCreateRequest {
    String name;
    String description;
    String formula;
    Boolean isAvailable;
}
