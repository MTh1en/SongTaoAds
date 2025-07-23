package com.capstone.ads.dto.custom_design_request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomDesignRequestFinalDesignRequest {
    MultipartFile finalDesignImage;
    List<MultipartFile> subFinalDesignImages;
}
