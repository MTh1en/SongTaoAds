package com.capstone.ads.dto.producttypesize;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductTypeSizeDTO {
    private String id;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String productTypeId;
    private String sizeId;
}
