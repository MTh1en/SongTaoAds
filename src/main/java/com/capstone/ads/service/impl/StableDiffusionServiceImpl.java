package com.capstone.ads.service.impl;

import com.capstone.ads.dto.file.FileData;
import com.capstone.ads.dto.stablediffusion.TextToImageRequest;
import com.capstone.ads.dto.stablediffusion.controlnet.AlwaysonScripts;
import com.capstone.ads.dto.stablediffusion.controlnet.Args;
import com.capstone.ads.dto.stablediffusion.pendingtask.PendingTaskResponse;
import com.capstone.ads.dto.stablediffusion.progress.ProgressRequest;
import com.capstone.ads.dto.stablediffusion.progress.ProgressResponse;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.StableDiffusionMapper;
import com.capstone.ads.model.DesignTemplates;
import com.capstone.ads.repository.external.S3Repository;
import com.capstone.ads.repository.external.StableDiffusionRepository;
import com.capstone.ads.repository.internal.DesignTemplatesRepository;
import com.capstone.ads.service.StableDiffusionService;
import com.capstone.ads.utils.DataConverter;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StableDiffusionServiceImpl implements StableDiffusionService {
    @Value("${stable-diffusion.token}")
    private String stableDiffusionToken;
    @Value("${aws.bucket.name}")
    private String bucketName;

    private final StableDiffusionRepository stableDiffusionRepository;
    private final DesignTemplatesRepository designTemplatesRepository;
    private final S3Repository s3Repository;
    private final SecurityContextUtils securityContextUtils;
    private final StableDiffusionMapper stableDiffusionMapper;

    @Override
    public FileData generateImage(String designTemplateId, String prompt) {
        String bearerStableDiffusionToken = generateBearerStableDiffusionToken();
        String userId = securityContextUtils.getCurrentUserId();

        String imageBase64 = getImageBytesFromDesignTemplate(designTemplateId);
        Args controlNetArgs = stableDiffusionMapper.mapArgs(imageBase64);
        // Xây dựng AlwaysonScripts
        AlwaysonScripts alwaysonScripts = stableDiffusionMapper.mapAlwaysonScripts(controlNetArgs);

        // Xây dựng TextToImageRequest
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

    private DesignTemplates findDesignTemplateById(String designTemplateId) {
        return designTemplatesRepository.findById(designTemplateId)
                .orElseThrow(() -> new AppException(ErrorCode.DESIGN_TEMPLATE_NOT_FOUND));
    }


    private String getImageBytesFromDesignTemplate(String designTemplateId) {
        var designTemplate = findDesignTemplateById(designTemplateId);
        byte[] imageBytes = s3Repository.downloadImageAsBytes(bucketName, designTemplate.getImage());
        return DataConverter.convertByteArrayToBase64(imageBytes);
    }
}
