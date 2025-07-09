package com.capstone.ads.dto.cost_type;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CostTypeUpdateRequest {
    @NotBlank(message = "Name is required")
    String name;
    String description;
    @NotBlank(message = "Formula is required")
    String formula;
    @Range(min = 0, max = 100, message = "Priority is Integer")
    Integer priority;
    Boolean isCore;
    Boolean isAvailable;
}
