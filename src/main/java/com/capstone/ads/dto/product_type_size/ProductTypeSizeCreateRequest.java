package com.capstone.ads.dto.product_type_size;

import com.capstone.ads.model.enums.DimensionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductTypeSizeCreateRequest {
    @NotNull
    @Positive
    Float maxValue;

    @NotNull
    @Positive
    Float minValue;

    @NotNull
    DimensionType dimensionType;
}
