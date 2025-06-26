package com.capstone.ads.dto.cost_type;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CostTypeUpdateRequest {
    String name;
    String description;
    String formula;
    Integer priority;
    Boolean isCore;
    Boolean isAvailable;
}
