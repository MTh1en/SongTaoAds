package com.capstone.ads.dto.size;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeCreateRequest {
    @NotBlank(message = "Size Name must is Required")
    String name;
    @NotBlank(message = "Product Type Calculate Formula is Required")
    String description;
}
