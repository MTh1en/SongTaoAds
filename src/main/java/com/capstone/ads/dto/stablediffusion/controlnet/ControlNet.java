package com.capstone.ads.dto.stablediffusion.controlnet;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ControlNet {
    List<Args> args;
}

