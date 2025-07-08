package com.capstone.ads.dto.edited_design;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditedDesignCreateRequest {
    @NotBlank(message = "Customer note is required")
    String customerNote;
    MultipartFile editedImage;
}
