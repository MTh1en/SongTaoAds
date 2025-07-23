package com.capstone.ads.dto.demo_design;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerRejectCustomDesignRequest {
    @NotBlank(message = "Customer Note is Required")
    String customerNote;
    MultipartFile feedbackImage;
}
