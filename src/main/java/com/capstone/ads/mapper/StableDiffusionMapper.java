package com.capstone.ads.mapper;

import com.capstone.ads.dto.stablediffusion.TextToImageRequest;
import com.capstone.ads.dto.stablediffusion.controlnet.AlwaysonScripts;
import com.capstone.ads.dto.stablediffusion.controlnet.Args;
import com.capstone.ads.dto.stablediffusion.controlnet.ControlNet;
import com.capstone.ads.dto.stablediffusion.progress.ProgressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;

@Mapper(componentModel = "spring")
public interface StableDiffusionMapper {
    @Mapping(target = "image", source = "imageBase64")
    Args mapArgs(String imageBase64);

    @Mapping(target = "forceTaskId", source = "userId")
    TextToImageRequest mapTextToImageRequest(String prompt, AlwaysonScripts alwaysonScripts, String userId);

    @Mapping(target = "idTask", source = "taskId")
    ProgressRequest mapProgressRequest(String taskId);

    default AlwaysonScripts mapAlwaysonScripts(Args args) {
        return AlwaysonScripts.builder()
                .controlNet(ControlNet.builder()
                        .args(Collections.singletonList(args))
                        .build())
                .build();

    }
}
