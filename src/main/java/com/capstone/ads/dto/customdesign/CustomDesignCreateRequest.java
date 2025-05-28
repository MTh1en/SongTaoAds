package com.capstone.ads.dto.customdesign;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomDesignCreateRequest {
    String designerDescription;
    MultipartFile image;
}
