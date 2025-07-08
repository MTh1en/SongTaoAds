package com.capstone.ads.dto.design_template;

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
    String negativePrompt;
    Integer width;
    Integer height;
    Boolean isAvailable;
}
