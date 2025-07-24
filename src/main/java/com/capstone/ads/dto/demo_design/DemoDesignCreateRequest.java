package com.capstone.ads.dto.demo_design;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemoDesignCreateRequest {
    String designerDescription;
    MultipartFile customDesignImage;
    List<MultipartFile> subCustomDesignImage;
}
