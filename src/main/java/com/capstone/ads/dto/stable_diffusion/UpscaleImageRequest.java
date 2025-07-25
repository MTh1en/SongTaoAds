package com.capstone.ads.dto.stable_diffusion;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpscaleImageRequest {
    @Builder.Default
    Integer resizeMode = 1;

    @Builder.Default
    Boolean showExtrasResults = true;

    Integer upscalingResizeW;
    Integer upscalingResizeH;

    @Builder.Default
    Boolean upscalingCrop = true;

    @Builder.Default
    @JsonProperty(value = "upscaler_1")
    String upscaler1 = "R-ESRGAN 4x+";

    @Builder.Default
    Boolean upscaleFirst = false;

    String image;
}
