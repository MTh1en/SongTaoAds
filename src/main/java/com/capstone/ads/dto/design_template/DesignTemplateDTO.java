package com.capstone.ads.dto.design_template;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DesignTemplateDTO {
    String id;
    String name;
    String description;
    String image;
    String negativePrompt;
    Integer width;
    Integer height;
    Boolean isAvailable;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String userId;
    String productTypeId;
}
