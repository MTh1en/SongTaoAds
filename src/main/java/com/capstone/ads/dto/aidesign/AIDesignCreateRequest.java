package com.capstone.ads.dto.aidesign;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AIDesignCreateRequest {
    String customerNote;
    MultipartFile image;
}
