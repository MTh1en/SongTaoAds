package com.capstone.ads.service.impl;

import com.capstone.ads.dto.file.FileInformation;
import com.capstone.ads.dto.stable_diffusion.TextToImageRequest;
import com.capstone.ads.dto.stable_diffusion.controlnet.AlwaysonScripts;
import com.capstone.ads.dto.stable_diffusion.controlnet.Args;
import com.capstone.ads.dto.stable_diffusion.pendingtask.PendingTaskResponse;
import com.capstone.ads.dto.stable_diffusion.progress.ProgressRequest;
import com.capstone.ads.dto.stable_diffusion.progress.ProgressResponse;
import com.capstone.ads.mapper.StableDiffusionMapper;
import com.capstone.ads.repository.external.StableDiffusionRepository;
import com.capstone.ads.service.DesignTemplatesService;
import com.capstone.ads.service.S3Service;
import com.capstone.ads.service.StableDiffusionService;
import com.capstone.ads.utils.DataConverter;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StableDiffusionServiceImpl implements StableDiffusionService {
    @NonFinal
    @Value("${stable-diffusion.token}")
    private String stableDiffusionToken;

    @NonFinal
    @Value("${stable-diffusion.model-checkpoint}")
    String modelCheckpoint;

    @NonFinal
    @Value("${stable-diffusion.controlnet.module}")
    String controlnetModule;

    @NonFinal
    @Value("${stable-diffusion.controlnet.model}")
    String controlnetModel;

    DesignTemplatesService designTemplatesService;
    S3Service s3Service;
    StableDiffusionRepository stableDiffusionRepository;
    StableDiffusionMapper stableDiffusionMapper;
    SecurityContextUtils securityContextUtils;

    @Override
    public FileInformation generateImage(String designTemplateId, String prompt) {
        String bearerStableDiffusionToken = generateBearerStableDiffusionToken();
        String userId = securityContextUtils.getCurrentUserId();

        String imageBase64 = getImageBytesFromDesignTemplate(designTemplateId);
        Args controlNetArgs = stableDiffusionMapper.mapArgs(imageBase64, controlnetModule, controlnetModel);

        AlwaysonScripts alwaysonScripts = stableDiffusionMapper.mapAlwaysonScripts(controlNetArgs);

        Map<String, Object> overrideSettings = new HashMap<>() {
            {
                put("sd_model_checkpoint", modelCheckpoint);
            }
        };

        // Xây dựng TextToImageRequest
        if (Strings.isBlank(prompt)) {
            prompt = "A simple advertising 2d background";
        }

        TextToImageRequest request = stableDiffusionMapper.mapTextToImageRequest(prompt, alwaysonScripts, userId, overrideSettings);
        var response = stableDiffusionRepository.textToImage(bearerStableDiffusionToken, request);
        String base64OutputImage = response.getImages().getFirst();
        byte[] outputImageBytes = DataConverter.convertBase64ToByteArray(base64OutputImage);

        return FileInformation.builder()
                .content(outputImageBytes)
                .contentType(MediaType.IMAGE_PNG_VALUE)
                .build();
    }

    @Override
    public ProgressResponse checkProgressByTaskId() {
        String taskId = securityContextUtils.getCurrentUserId();
        String bearerStableDiffusionToken = generateBearerStableDiffusionToken();

        ProgressRequest request = stableDiffusionMapper.mapProgressRequest(taskId);
        log.info("checkProgressByTaskId: {}", request);
        return stableDiffusionRepository.checkProgress(bearerStableDiffusionToken, request);
    }

    @Override
    public PendingTaskResponse checkPendingTask() {
        String bearerStableDiffusionToken = generateBearerStableDiffusionToken();
        return stableDiffusionRepository.checkPendingTask(bearerStableDiffusionToken);
    }

    private String generateBearerStableDiffusionToken() {
        return String.format("Bearer %s", stableDiffusionToken);
    }


    private String getImageBytesFromDesignTemplate(String designTemplateId) {
        var designTemplate = designTemplatesService.getDesignTemplateById(designTemplateId);
        FileInformation imageFileInformation = s3Service.downloadFile(designTemplate.getImage());
        return DataConverter.convertByteArrayToBase64(imageFileInformation.getContent());
    }
}
