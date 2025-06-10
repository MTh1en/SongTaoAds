package com.capstone.ads.dto.designtemplate;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DesignTemplateUpdateRequest {
    String name;
    String description;
    String negativePrompt;
    Integer width;
    Integer height;
    Boolean isAvailable;
}
