package com.capstone.ads.dto.stable_diffusion;

import com.capstone.ads.dto.stable_diffusion.controlnet.AlwaysonScripts;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TextToImageRequest {
    @Builder.Default
    String prompt = "A simple advertising 2d background";

    @Builder.Default
    String negativePrompt = """
            (nsfw, naked, nude, deformed iris, deformed pupils, semi-realistic, 
            cgi, 3d, render, sketch, cartoon, drawing, anime, mutated hands and fingers:1.4), 
            (deformed, distorted, disfigured:1.3), 
            poorly drawn, bad anatomy, wrong anatomy, extra limb, missing limb, floating limbs, disconnected limbs, 
            mutation, mutated, ugly, disgusting, amputation, text, fictional structure
            """;

    @Builder.Default
    Integer cfgScale = 5;

    @Builder.Default
    Integer batchSize = 1;

    @Builder.Default
    Integer nIter = 1;

    @Builder.Default
    Integer steps = 25;

    @Builder.Default
    Integer width = 1024;

    @Builder.Default
    Integer height = 512;

    @Builder.Default
    Boolean restoreFaces = false;

    @Builder.Default
    Boolean sendImages = true;

    Map<String, Object> overrideSettings = new HashMap<>();

    @Builder.Default
    String samplerName = "Euler";

    @Builder.Default
    String scheduler = "simple";

    String forceTaskId;

    AlwaysonScripts alwaysonScripts;
}
