package com.capstone.ads.dto.design_template;

import com.capstone.ads.model.enums.AspectRatio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DesignTemplateCreateRequest {
    @NotBlank(message = "Name is Required")
    String name;
    String description;
    AspectRatio aspectRatio;
    Boolean isAvailable;
    MultipartFile designTemplateImage;
}
