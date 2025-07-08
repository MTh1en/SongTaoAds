package com.capstone.ads.dto.attribute_value;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributeValuesUpdateRequest {
    @Size(min = 6, message = "Attribute Value Name must be at least 6 characters")
    String name;
    @NotBlank(message = "Attribute Value Unit is Required")
    String unit;
    Long materialPrice;
    @Positive(message = "Unit Price must be greater than 0")
    Long unitPrice;
    Boolean isAvailable;
}
