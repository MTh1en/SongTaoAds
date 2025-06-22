package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageDuration;
import com.capstone.ads.dto.aidesign.AIDesignDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.AIDesignsMapper;
import com.capstone.ads.model.AIDesigns;
import com.capstone.ads.repository.internal.AIDesignsRepository;
import com.capstone.ads.service.AIDesignsService;
import com.capstone.ads.service.CustomerDetailService;
import com.capstone.ads.service.DesignTemplatesService;
import com.capstone.ads.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIDesignsServiceImpl implements AIDesignsService {
    private final CustomerDetailService customerDetailService;
    private final DesignTemplatesService designTemplatesService;
    private final S3Service s3Service;
    private final AIDesignsRepository aiDesignsRepository;
    private final AIDesignsMapper aiDesignsMapper;

    @Override
    @Transactional
    public AIDesignDTO createAIDesign(String customerDetailId, String designTemplateId, String customerNote, MultipartFile aiImage) {
        customerDetailService.validateCustomerDetailExists(customerDetailId);
        designTemplatesService.validateDesignTemplateExistsAndAvailable(designTemplateId);

        AIDesigns aiDesigns = aiDesignsMapper.toEntity(customerDetailId, designTemplateId, customerNote);
        String aiDesignImageUrl = uploadAIDesignImageToS3(customerDetailId, aiImage);
        aiDesigns.setImage(aiDesignImageUrl);

        aiDesigns = aiDesignsRepository.save(aiDesigns);
        return aiDesignsMapper.toDTO(aiDesigns);
    }

    @Override
    public Page<AIDesignDTO> findAIDesignByCustomerDetailId(String customerDetailId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return aiDesignsRepository.findByCustomerDetail_Id(customerDetailId, pageable)
                .map(aiDesignsMapper::toDTO);
    }

    @Override
    @Transactional
    public void hardDeleteAIDesign(String AIDesignId) {
        if (!aiDesignsRepository.existsById(AIDesignId)) {
            throw new AppException(ErrorCode.AI_DESIGN_NOT_FOUND);
        }
        aiDesignsRepository.deleteById(AIDesignId);
    }

    @Override
    public AIDesigns getAIDesignById(String AIDesignId) {
        return aiDesignsRepository.findById(AIDesignId)
                .orElseThrow(() -> new AppException(ErrorCode.AI_DESIGN_NOT_FOUND));
    }

    private AIDesignDTO convertToAIDesignDTOWithImageIsPresignedURL(AIDesigns aiDesigns) {
        var aiDesignDTOResponse = aiDesignsMapper.toDTO(aiDesigns);
        if (!Objects.isNull(aiDesigns.getImage())) {
            var designTemplateImagePresigned = s3Service.getPresignedUrl(aiDesigns.getImage(), S3ImageDuration.CUSTOM_DESIGN_DURATION);
            aiDesignDTOResponse.setImage(designTemplateImagePresigned);
        }
        return aiDesignDTOResponse;
    }

    private String generateAIDesignKey(String customerDetailId) {
        return String.format("ai-designs/%s/%s", customerDetailId, UUID.randomUUID());
    }

    private String uploadAIDesignImageToS3(String customerDetailId, MultipartFile file) {
        String AIDesignImageKey = generateAIDesignKey(customerDetailId);
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        s3Service.uploadSingleFile(AIDesignImageKey, file);
        return AIDesignImageKey;
    }
}
