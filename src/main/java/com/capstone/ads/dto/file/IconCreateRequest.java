package com.capstone.ads.dto.file;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IconCreateRequest {
    String name;
    String description;
    MultipartFile iconImage;
}
