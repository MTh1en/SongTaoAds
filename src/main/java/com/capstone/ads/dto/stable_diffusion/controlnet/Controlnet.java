package com.capstone.ads.dto.stable_diffusion.controlnet;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Controlnet {
    List<Args> args;
}

