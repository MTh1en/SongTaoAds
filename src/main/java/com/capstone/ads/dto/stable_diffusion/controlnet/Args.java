package com.capstone.ads.dto.stable_diffusion.controlnet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Args {
    @Builder.Default
    Boolean enabled = true;

    String image;
    String module;
    String model;

    @Builder.Default
    Double weight = 0.8;

    @Builder.Default
    String resizeMode = "Just Resize";

    @Builder.Default
    Boolean lowVram = false;

    @Builder.Default
    Double processorRes = 512.0;

    @Builder.Default
    Double guidanceStart = 0.0;

    @Builder.Default
    Double guidanceEnd = 1.0;

    @Builder.Default
    Boolean pixelPerfect = false;

    @Builder.Default
    String controlMode = "Controlnet is more important";
}
