package com.capstone.ads.dto.stable_diffusion.controlnet;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AlwaysonScripts {
    Controlnet controlnet;
}
