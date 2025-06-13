package com.capstone.ads.mapper;

import com.capstone.ads.dto.stablediffusion.TextToImageRequest;
import com.capstone.ads.dto.stablediffusion.controlnet.AlwaysonScripts;
import com.capstone.ads.dto.stablediffusion.controlnet.Args;
import com.capstone.ads.dto.stablediffusion.controlnet.ControlNet;
import com.capstone.ads.dto.stablediffusion.progress.ProgressRequest;
import org.apache.logging.log4j.util.Strings;
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
