package com.capstone.ads.dto.stable_diffusion.controlnet;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AlwaysonScripts {
    @JsonProperty(value = "controlnet")
    ControlNet controlNet;
}
