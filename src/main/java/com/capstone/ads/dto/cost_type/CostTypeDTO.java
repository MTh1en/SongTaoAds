package com.capstone.ads.dto.cost_type;

import com.capstone.ads.dto.product_type.ProductTypeDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CostTypeDTO {
    String id;
    String name;
    String description;
    String formula;
    Boolean isAvailable;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    ProductTypeDTO productTypes;
}
