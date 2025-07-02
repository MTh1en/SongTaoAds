package com.capstone.ads.dto.attribute;

import com.capstone.ads.dto.CoreDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributesDTO {
    String id;
    String name;
    String calculateFormula;
    Boolean isAvailable;
    Boolean isCore;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    CoreDTO productTypeId;
}
