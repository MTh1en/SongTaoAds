package com.capstone.ads.dto.stablediffusion;

import com.capstone.ads.dto.stablediffusion.controlnet.AlwaysonScripts;
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
    String prompt;

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
    Integer steps = 25;

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
            put("sd_model_checkpoint", "dreamshaperXL_v21TurboDPMSDE.safetensors");
        }
    };

    @Builder.Default
    @JsonProperty("sampler_name")
    String samplerName = "DPM++ 2M SDE";

    @Builder.Default
    String scheduler = "karras";

    @JsonProperty(value = "force_task_id")
    String forceTaskId;

    @JsonProperty(value = "alwayson_scripts")
    AlwaysonScripts alwaysonScripts;
}
