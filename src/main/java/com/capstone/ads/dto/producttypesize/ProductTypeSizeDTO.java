package com.capstone.ads.dto.producttypesize;

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
    private String id;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String productTypeId;
    private SizeDTO size;
}
