package com.capstone.ads.dto.customer_choice_size;

import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerChoicesSizeCreateRequest {
    @Positive(message = "Size Value must greater than 0")
    Double sizeValue;
}
