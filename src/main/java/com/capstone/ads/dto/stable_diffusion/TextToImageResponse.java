package com.capstone.ads.dto.stable_diffusion;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TextToImageResponse {
    List<String> images;
    Object parameters;
    String info;
}
