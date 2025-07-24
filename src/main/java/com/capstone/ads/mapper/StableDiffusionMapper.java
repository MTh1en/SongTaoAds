package com.capstone.ads.mapper;

import com.capstone.ads.dto.stable_diffusion.TextToImageRequest;
import com.capstone.ads.dto.stable_diffusion.UpscaleImageRequest;
import com.capstone.ads.dto.stable_diffusion.controlnet.AlwaysonScripts;
import com.capstone.ads.dto.stable_diffusion.controlnet.Args;
import com.capstone.ads.dto.stable_diffusion.controlnet.Controlnet;
import com.capstone.ads.dto.stable_diffusion.progress.ProgressRequest;
import org.mapstruct.Mapper;

import java.util.Collections;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface StableDiffusionMapper {
    Args mapArgs(String image, String module, String model);

    TextToImageRequest mapTextToImageRequest(String prompt, Integer width, Integer height, AlwaysonScripts alwaysonScripts, String forceTaskId,
                                             Map<String, Object> overrideSettings);

    ProgressRequest mapProgressRequest(String idTask);

    default AlwaysonScripts mapAlwaysonScripts(Args args) {
        return AlwaysonScripts.builder()
                .controlnet(Controlnet.builder()
                        .args(Collections.singletonList(args))
                        .build())
                .build();

    }

    UpscaleImageRequest mapUpscaleImageRequest(String image, Integer upscalingResizeW, Integer upscalingResizeH);
}
