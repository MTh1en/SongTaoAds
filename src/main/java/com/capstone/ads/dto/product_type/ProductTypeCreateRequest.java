package com.capstone.ads.dto.product_type;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductTypeCreateRequest {
    @Size(min = 6, message = "Product Type Name must be at least 6 characters")
    String name;
    Boolean isAiGenerated;
    Boolean isAvailable;
    MultipartFile productTypeImage;
}
