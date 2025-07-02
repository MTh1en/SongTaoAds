package com.capstone.ads.dto.background;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BackgroundCreateRequest {
    @NotBlank(message = "Name is required")
    String name;
    String description;
    MultipartFile backgroundImage;
}
