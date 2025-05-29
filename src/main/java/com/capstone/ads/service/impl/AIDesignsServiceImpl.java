package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageDuration;
import com.capstone.ads.dto.aidesign.AIDesignDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.AIDesignsMapper;
import com.capstone.ads.model.AIDesigns;
import com.capstone.ads.repository.external.S3Repository;
import com.capstone.ads.repository.internal.AIDesignsRepository;
import com.capstone.ads.repository.internal.CustomerDetailRepository;
import com.capstone.ads.repository.internal.DesignTemplatesRepository;
import com.capstone.ads.service.AIDesignsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIDesignsServiceImpl implements AIDesignsService {
    @Value("${aws.bucket.name}")
    private String bucketName;

    private final CustomerDetailRepository customerDetailRepository;
    private final DesignTemplatesRepository designTemplatesRepository;
    private final S3Repository s3Repository;
    private final AIDesignsRepository aiDesignsRepository;
    private final AIDesignsMapper aiDesignsMapper;

    @Override
    @Transactional
    public AIDesignDTO createAIDesign(String customerDetailId, String designTemplateId, String customerNote, MultipartFile aiImage) {
        customerDetailRepository.findById(customerDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_DETAIL_NOT_FOUND));
        designTemplatesRepository.findByIdAndIsAvailable(designTemplateId, true)
                .orElseThrow(() -> new AppException(ErrorCode.DESIGN_TEMPLATE_NOT_FOUND));
        AIDesigns aiDesigns = aiDesignsMapper.toEntity(customerDetailId, designTemplateId, customerNote);
        String aiDesignImageUrl = uploadAIDesignImageToS3(customerDetailId, aiImage);
        aiDesigns.setImage(aiDesignImageUrl);
        aiDesigns = aiDesignsRepository.save(aiDesigns);
        return aiDesignsMapper.toDTO(aiDesigns);
    }

    @Override
    public List<AIDesignDTO> findAIDesignByCustomerDetailId(String customerDetailId) {
        return aiDesignsRepository.findByCustomerDetail_Id(customerDetailId).stream()
                .map(this::convertToAIDesignDTOWithImageIsPresignedURL)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void hardDeleteAIDesign(String AIDesignId) {
        if (!aiDesignsRepository.existsById(AIDesignId)) {
            throw new AppException(ErrorCode.AI_DESIGN_NOT_FOUND);
        }
        aiDesignsRepository.deleteById(AIDesignId);
    }

    private AIDesignDTO convertToAIDesignDTOWithImageIsPresignedURL(AIDesigns aiDesigns) {
        var aiDesignDTOResponse = aiDesignsMapper.toDTO(aiDesigns);
        if (!Objects.isNull(aiDesigns.getImage())) {
            var designTemplateImagePresigned = s3Repository.generatePresignedUrl(bucketName, aiDesigns.getImage(), S3ImageDuration.CUSTOM_DESIGN_DURATION);
            aiDesignDTOResponse.setImage(designTemplateImagePresigned);
        }
        return aiDesignDTOResponse;
    }

    private String generateAIDesignKey(String customerDetailId) {
        return String.format("/ai-designs/%s/%s", customerDetailId, UUID.randomUUID());
    }

    private String uploadAIDesignImageToS3(String customerDetailId, MultipartFile file) {
        String AIDesignImageKey = generateAIDesignKey(customerDetailId);
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        s3Repository.uploadSingleFile(bucketName, file, AIDesignImageKey);
        return AIDesignImageKey;
    }
}
