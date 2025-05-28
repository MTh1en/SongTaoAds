package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageDuration;
import com.capstone.ads.dto.customdesign.CustomDesignCreateRequest;
import com.capstone.ads.dto.customdesign.CustomDesignDTO;
import com.capstone.ads.dto.customdesign.CustomerDecisionCustomDesignRequest;
import com.capstone.ads.dto.customdesign.DesignerUpdateDescriptionCustomDesignRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomDesignsMapper;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.CustomDesigns;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import com.capstone.ads.model.enums.CustomDesignStatus;
import com.capstone.ads.repository.external.S3Repository;
import com.capstone.ads.repository.internal.CustomDesignRequestsRepository;
import com.capstone.ads.repository.internal.CustomDesignsRepository;
import com.capstone.ads.service.CustomDesignsService;
import com.capstone.ads.utils.CustomDesignStateValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomDesignsServiceImpl implements CustomDesignsService {
    @Value("${aws.bucket.name}")
    private String bucketName;

    private final CustomDesignRequestsRepository customDesignRequestsRepository;
    private final S3Repository s3Repository;
    private final CustomDesignsRepository customDesignsRepository;
    private final CustomDesignsMapper customDesignsMapper;
    private final CustomDesignStateValidator customDesignStateValidator;

    @Override
    @Transactional
    public CustomDesignDTO designerCreateCustomDesign(String customDesignRequestId, CustomDesignCreateRequest request) {
        var customDesignRequest = customDesignRequestsRepository.findByIdAndStatus(customDesignRequestId, CustomDesignRequestStatus.PROCESSING)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOM_DESIGN_REQUEST_NOT_FOUND));

        validateCreateRequest(customDesignRequest);

        CustomDesigns customDesigns = customDesignsMapper.toEntity(request, customDesignRequestId);
        customDesigns = customDesignsRepository.save(customDesigns);
        return customDesignsMapper.toDTO(customDesigns);
    }

    @Override
    @Transactional
    public CustomDesignDTO customerDecisionCustomDesign(String customDesignId, CustomerDecisionCustomDesignRequest request) {
        CustomDesigns customDesigns = findCustomDesignByIdAndPendingStatus(customDesignId);

        customDesignStateValidator.validateTransition(
                customDesigns.getStatus(),
                request.getStatus()
        );

        customDesignsMapper.updateEntityFromCustomerRequest(request, customDesigns);
        customDesigns = customDesignsRepository.save(customDesigns);
        return customDesignsMapper.toDTO(customDesigns);
    }

    @Override
    @Transactional
    public CustomDesignDTO designerUpdateDescriptionCustomDesign(String customDesignId, DesignerUpdateDescriptionCustomDesignRequest request) {
        CustomDesigns customDesigns = findCustomDesignByIdAndPendingStatus(customDesignId);
        customDesignsMapper.updateEntityFromDesignerRequest(request, customDesigns);

        customDesigns = customDesignsRepository.save(customDesigns);
        return customDesignsMapper.toDTO(customDesigns);
    }

    @Override
    @Transactional
    public CustomDesignDTO designerUploadImage(String customDesignId, MultipartFile file) {
        CustomDesigns customDesigns = findCustomDesignByIdAndPendingStatus(customDesignId);
        String customDesignImageKey = generateCustomDesignKey(customDesignId);

        s3Repository.uploadSingleFile(bucketName, file, customDesignImageKey);
        customDesigns.setImage(customDesignImageKey);

        customDesigns = customDesignsRepository.save(customDesigns);
        return customDesignsMapper.toDTO(customDesigns);
    }

    @Override
    public List<CustomDesignDTO> findCustomDesignByCustomDesignRequest(String customDesignRequestId) {
        return customDesignsRepository.findByCustomDesignRequests_Id(customDesignRequestId).stream()
                .map(this::convertToCustomDesignDTOWithImageIsPresignedURL)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void hardDeleteCustomDesign(String customDesignId) {
        if (!customDesignsRepository.existsById(customDesignId)) {
            throw new AppException(ErrorCode.CUSTOM_DESIGN_NOT_FOUND);
        }
        customDesignsRepository.deleteById(customDesignId);
    }

    private CustomDesigns findCustomDesignByIdAndPendingStatus(String customDesignId) {
        return customDesignsRepository.findByIdAndStatus(customDesignId, CustomDesignStatus.PENDING)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOM_DESIGN_NOT_FOUND));
    }

    private String generateCustomDesignKey(String customDesignId) {
        return String.format("custom-design/%s/%s", customDesignId, UUID.randomUUID());
    }


    private CustomDesignDTO convertToCustomDesignDTOWithImageIsPresignedURL(CustomDesigns customDesigns) {
        var customDesignDTOResponse = customDesignsMapper.toDTO(customDesigns);
        if (!Objects.isNull(customDesigns.getImage())) {
            var designTemplateImagePresigned = s3Repository.generatePresignedUrl(bucketName, customDesigns.getImage(), S3ImageDuration.CUSTOM_DESIGN_DURATION);
            customDesignDTOResponse.setImage(designTemplateImagePresigned);
        }
        return customDesignDTOResponse;
    }

    private void validateCreateRequest(CustomDesignRequests customDesignRequests) {
        List<CustomDesignStatus> allowedStatuses = Arrays.asList(
                CustomDesignStatus.PENDING,
                CustomDesignStatus.APPROVED
        );
        boolean customDesignExists = customDesignsRepository.existsByCustomDesignRequests_IdAndStatusIn(customDesignRequests.getId(), allowedStatuses);

        if (customDesignExists) {
            throw new AppException(ErrorCode.CUSTOM_DESIGN_IN_WAITING_DECISION_FROM_CUSTOMER);
        }
    }
}
