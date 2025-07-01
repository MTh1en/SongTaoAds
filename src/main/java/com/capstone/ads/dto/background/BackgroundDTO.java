package com.capstone.ads.dto.background;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BackgroundDTO {
    String id;
    String name;
    String description;
    String backgroundUrl;
    Boolean isAvailable;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    BackgroundAttributeValueDTO attributeValues;
}
