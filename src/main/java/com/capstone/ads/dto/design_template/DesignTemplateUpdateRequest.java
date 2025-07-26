package com.capstone.ads.dto.design_template;

import com.capstone.ads.model.enums.AspectRatio;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DesignTemplateUpdateRequest {
    @NotBlank(message = "Name is Required")
    String name;
    String description;
    AspectRatio aspectRatio;
    Boolean isAvailable;
}
