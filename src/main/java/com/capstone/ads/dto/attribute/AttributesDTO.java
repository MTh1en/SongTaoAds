package com.capstone.ads.dto.attribute;

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
    LocalDateTime createAt;
    LocalDateTime updateAt;
    String productTypeId;
}
