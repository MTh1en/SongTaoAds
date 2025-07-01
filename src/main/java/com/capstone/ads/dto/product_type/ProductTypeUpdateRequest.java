package com.capstone.ads.dto.product_type;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductTypeUpdateRequest {
    @Size(min = 6, message = "Product Type Name must be at least 6 characters")
    String name;
    @NotBlank(message = "Product Type Calculate Formula is Required")
    String calculateFormula;
    Boolean isAiGenerated;
    Boolean isAvailable;
}
