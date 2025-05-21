package com.capstone.ads.dto.attribute;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributesCreateRequest {
    @Size(min = 6, message = "Attribute Name must be at least 6 characters")
    String name;
    @NotBlank(message = "Attribute Calculate Formula is Required")
    String calculateFormula;
    Boolean isAvailable;
    Boolean isCore;
}
