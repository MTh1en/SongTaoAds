package com.capstone.ads.dto.product_type_size;

import com.capstone.ads.dto.size.SizeDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductTypeSizeDTO {
    String id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String productTypeId;
    SizeDTO sizes;
}
