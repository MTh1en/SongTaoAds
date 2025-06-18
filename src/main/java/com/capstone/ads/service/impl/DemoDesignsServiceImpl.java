package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageDuration;
import com.capstone.ads.dto.demo_design.DemoDesignDTO;
import com.capstone.ads.dto.demo_design.CustomerDecisionCustomDesignRequest;
import com.capstone.ads.dto.demo_design.DesignerUpdateDescriptionCustomDesignRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.DemoDesignsMapper;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.DemoDesigns;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import com.capstone.ads.model.enums.DemoDesignStatus;
import com.capstone.ads.repository.external.S3Repository;
import com.capstone.ads.repository.internal.CustomDesignRequestsRepository;
import com.capstone.ads.repository.internal.DemoDesignsRepository;
import com.capstone.ads.service.DemoDesignsService;
import com.capstone.ads.utils.DemoDesignStateValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DemoDesignsServiceImpl implements DemoDesignsService {
    @Value("${aws.bucket.name}")
    private String bucketName;

    private final CustomDesignRequestsRepository customDesignRequestsRepository;
    private final S3Repository s3Repository;
    private final DemoDesignsRepository demoDesignsRepository;
    private final DemoDesignsMapper demoDesignsMapper;
    private final DemoDesignStateValidator demoDesignStateValidator;

    @Override
    @Transactional
    public DemoDesignDTO designerCreateCustomDesign(String customDesignRequestId, String designerDescription, MultipartFile customDesignImage) {
        var customDesignRequest = customDesignRequestsRepository.findByIdAndStatus(customDesignRequestId, CustomDesignRequestStatus.PROCESSING)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOM_DESIGN_REQUEST_NOT_FOUND));

        validateCreateRequest(customDesignRequest);

        DemoDesigns demoDesigns = demoDesignsMapper.toEntity(designerDescription, customDesignRequestId);

        String customDesignImageKey = uploadCustomDesignImageToS3(customDesignRequestId, customDesignImage);
        demoDesigns.setDemoImage(customDesignImageKey);
        demoDesigns = demoDesignsRepository.save(demoDesigns);
        return demoDesignsMapper.toDTO(demoDesigns);
    }

    @Override
    @Transactional
    public DemoDesignDTO customerDecisionCustomDesign(String customDesignId, CustomerDecisionCustomDesignRequest request) {
        DemoDesigns demoDesigns = findCustomDesignByIdAndPendingStatus(customDesignId);

        demoDesignStateValidator.validateTransition(
                demoDesigns.getStatus(),
                request.getStatus()
        );

        demoDesignsMapper.updateEntityFromCustomerRequest(request, demoDesigns);
        demoDesigns = demoDesignsRepository.save(demoDesigns);
        return demoDesignsMapper.toDTO(demoDesigns);
    }

    @Override
    @Transactional
    public DemoDesignDTO designerUpdateDescriptionCustomDesign(String customDesignId, DesignerUpdateDescriptionCustomDesignRequest request) {
        DemoDesigns demoDesigns = findCustomDesignByIdAndPendingStatus(customDesignId);
        demoDesignsMapper.updateEntityFromDesignerRequest(request, demoDesigns);

        demoDesigns = demoDesignsRepository.save(demoDesigns);
        return demoDesignsMapper.toDTO(demoDesigns);
    }

    @Override
    @Transactional
    public DemoDesignDTO designerUploadImage(String customDesignId, MultipartFile file) {
        DemoDesigns demoDesigns = findCustomDesignByIdAndPendingStatus(customDesignId);

        String customDesignRequestId = demoDesigns.getCustomDesignRequests().getId();
        String customDesignImageKey = uploadCustomDesignImageToS3(customDesignRequestId, file);
        demoDesigns.setDemoImage(customDesignImageKey);

        demoDesigns = demoDesignsRepository.save(demoDesigns);
        return demoDesignsMapper.toDTO(demoDesigns);
    }

    @Override
    public Page<DemoDesignDTO> findCustomDesignByCustomDesignRequest(String customDesignRequestId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return demoDesignsRepository.findByCustomDesignRequests_Id(customDesignRequestId, pageable)
                .map(this::convertToCustomDesignDTOWithImageIsPresignedURL);
    }

    @Override
    @Transactional
    public void hardDeleteCustomDesign(String customDesignId) {
        if (!demoDesignsRepository.existsById(customDesignId)) {
            throw new AppException(ErrorCode.CUSTOM_DESIGN_NOT_FOUND);
        }
        demoDesignsRepository.deleteById(customDesignId);
    }

    private DemoDesigns findCustomDesignByIdAndPendingStatus(String customDesignId) {
        return demoDesignsRepository.findByIdAndStatus(customDesignId, DemoDesignStatus.PENDING)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOM_DESIGN_NOT_FOUND));
    }

    private String generateCustomDesignKey(String customDesignRequestId) {
        return String.format("custom-design/%s/%s", customDesignRequestId, UUID.randomUUID());
    }


    private DemoDesignDTO convertToCustomDesignDTOWithImageIsPresignedURL(DemoDesigns demoDesigns) {
        var customDesignDTOResponse = demoDesignsMapper.toDTO(demoDesigns);
        if (!Objects.isNull(demoDesigns.getDemoImage())) {
            var designTemplateImagePresigned = s3Repository.generatePresignedUrl(bucketName, demoDesigns.getDemoImage(), S3ImageDuration.CUSTOM_DESIGN_DURATION);
            customDesignDTOResponse.setImage(designTemplateImagePresigned);
        }
        return customDesignDTOResponse;
    }

    private void validateCreateRequest(CustomDesignRequests customDesignRequests) {
        List<DemoDesignStatus> allowedStatuses = Arrays.asList(
                DemoDesignStatus.PENDING,
                DemoDesignStatus.APPROVED
        );
        boolean customDesignExists = demoDesignsRepository.existsByCustomDesignRequests_IdAndStatusIn(customDesignRequests.getId(), allowedStatuses);

        if (customDesignExists) {
            throw new AppException(ErrorCode.CUSTOM_DESIGN_IN_WAITING_DECISION_FROM_CUSTOMER);
        }
    }

    private String uploadCustomDesignImageToS3(String customDesignRequestId, MultipartFile file) {
        String customDesignImageKey = generateCustomDesignKey(customDesignRequestId);
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        s3Repository.uploadSingleFile(bucketName, file, customDesignImageKey);
        return customDesignImageKey;
    }
}
