package com.capstone.ads.service.impl;

import com.capstone.ads.dto.file.FileData;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StableDiffusionServiceImpl implements StableDiffusionService {
    @Value("${stable-diffusion.token}")
    private String stableDiffusionToken;

    private final DesignTemplatesService designTemplatesService;
    private final S3Service s3Service;
    private final StableDiffusionRepository stableDiffusionRepository;
    private final StableDiffusionMapper stableDiffusionMapper;
    private final SecurityContextUtils securityContextUtils;

    @Override
    public FileData generateImage(String designTemplateId, String prompt) {
        String bearerStableDiffusionToken = generateBearerStableDiffusionToken();
        String userId = securityContextUtils.getCurrentUserId();

        String imageBase64 = getImageBytesFromDesignTemplate(designTemplateId);
        Args controlNetArgs = stableDiffusionMapper.mapArgs(imageBase64);
        // Xây dựng AlwaysonScripts
        AlwaysonScripts alwaysonScripts = stableDiffusionMapper.mapAlwaysonScripts(controlNetArgs);

        // Xây dựng TextToImageRequest
        if (Strings.isBlank(prompt)) {
            prompt = "A simple advertising 2d background";
        }
        TextToImageRequest request = stableDiffusionMapper.mapTextToImageRequest(prompt, alwaysonScripts, userId);

        var response = stableDiffusionRepository.textToImage(bearerStableDiffusionToken, request);
        String base64OutputImage = response.getImages().getFirst();
        byte[] outputImageBytes = DataConverter.convertBase64ToByteArray(base64OutputImage);

        return FileData.builder()
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
        FileData imageFileData = s3Service.downloadFile(designTemplate.getImage());
        return DataConverter.convertByteArrayToBase64(imageFileData.getContent());
    }
}
