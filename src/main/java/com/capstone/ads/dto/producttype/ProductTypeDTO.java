package com.capstone.ads.dto.producttype;

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
    String name;
    String calculateFormula;
    Boolean isAvailable;
    LocalDateTime createAt;
    LocalDateTime updateAt;
}
