package com.capstone.ads.dto.file;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IconCreateRequest {
    @NotBlank(message = "Name is required")
    String name;
    String description;
    MultipartFile iconImage;
}
