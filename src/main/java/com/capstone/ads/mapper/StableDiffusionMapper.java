package com.capstone.ads.mapper;

import com.capstone.ads.dto.stable_diffusion.TextToImageRequest;
import com.capstone.ads.dto.stable_diffusion.controlnet.AlwaysonScripts;
import com.capstone.ads.dto.stable_diffusion.controlnet.Args;
import com.capstone.ads.dto.stable_diffusion.controlnet.ControlNet;
import com.capstone.ads.dto.stable_diffusion.progress.ProgressRequest;
import org.mapstruct.Mapper;

import java.util.Collections;

@Mapper(componentModel = "spring")
public interface StableDiffusionMapper {
    Args mapArgs(String image);


    TextToImageRequest mapTextToImageRequest(String prompt, AlwaysonScripts alwaysonScripts, String forceTaskId);

    ProgressRequest mapProgressRequest(String idTask);

    default AlwaysonScripts mapAlwaysonScripts(Args args) {
        return AlwaysonScripts.builder()
                .controlNet(ControlNet.builder()
                        .args(Collections.singletonList(args))
                        .build())
                .build();

    }
}
