package com.capstone.ads.dto.product_type;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductTypeDTO {
    String id;
    String image;
    String name;
    String calculateFormula;
    Boolean isAiGenerated;
    Boolean isAvailable;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
