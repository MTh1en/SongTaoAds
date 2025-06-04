package com.capstone.ads.dto.stablediffusion.controlnet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Args {
    @Builder.Default
    Boolean enabled = true;

    String image;

    @Builder.Default
    String module = "depth_anything_v2";
    @Builder.Default
    String model = "diffusers_xl_depth_full [2f51180b]";
    @Builder.Default
    Double weight = 0.8;

    @Builder.Default
    @JsonProperty(value = "resize_mode")
    String resizeMode = "Just Resize";

    @Builder.Default
    @JsonProperty(value = "low_vram")
    Boolean lowVram = false;

    @Builder.Default
    @JsonProperty(value = "processor_res")
    Double processorRes = 512.0;

    @Builder.Default
    @JsonProperty(value = "guidance_start")
    Double guidanceStart = 0.0;

    @Builder.Default
    @JsonProperty(value = "guidance_end")
    Double guidanceEnd = 1.0;

    @Builder.Default
    @JsonProperty(value = "pixel_perfect")
    Boolean pixelPerfect = false;

    @Builder.Default
    @JsonProperty(value = "control_mode")
    String controlMode = "Balanced";
}
