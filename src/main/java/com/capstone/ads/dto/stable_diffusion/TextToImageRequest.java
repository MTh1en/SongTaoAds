package com.capstone.ads.dto.stable_diffusion;

import com.capstone.ads.dto.stable_diffusion.controlnet.AlwaysonScripts;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TextToImageRequest {
    @Builder.Default
    String prompt = "A simple advertising 2d background";

    @Builder.Default
    @JsonProperty("negative_prompt")
    String negativePrompt = "(nsfw, naked, nude, deformed iris, deformed pupils, semi-realistic, cgi, 3d, render, sketch, cartoon, drawing, anime, mutated hands and fingers:1.4), (deformed, distorted, disfigured:1.3), poorly drawn, bad anatomy, wrong anatomy, extra limb, missing limb, floating limbs, disconnected limbs, mutation, mutated, ugly, disgusting, amputation";

    @Builder.Default
    @JsonProperty("cfg_scale")
    Integer cfgScale = 5;

    @Builder.Default
    @JsonProperty("batch_size")
    Integer batchSize = 1;

    @Builder.Default
    @JsonProperty("n_iter")
    Integer nIter = 1;

    @Builder.Default
    Integer steps = 30;

    @Builder.Default
    Double width = 1024.0;

    @Builder.Default
    Double height = 512.0;

    @Builder.Default
    @JsonProperty("restore_faces")
    Boolean restoreFaces = false;

    @Builder.Default
    @JsonProperty("send_images")
    Boolean sendImages = true;

    @Builder.Default
    @JsonProperty("override_settings")
    Map<String, Object> overrideSettings = new HashMap<>() {
        {
            put("sd_model_checkpoint", "dreamshaper_8.safetensors");
        }
    };

    @Builder.Default
    @JsonProperty("sampler_name")
    String samplerName = "Euler";

    @Builder.Default
    String scheduler = "simple";

    @JsonProperty(value = "force_task_id")
    String forceTaskId;

    @JsonProperty(value = "alwayson_scripts")
    AlwaysonScripts alwaysonScripts;
}
