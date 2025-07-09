package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageDuration;
import com.capstone.ads.dto.demo_design.DemoDesignCreateRequest;
import com.capstone.ads.dto.demo_design.DemoDesignDTO;
import com.capstone.ads.dto.demo_design.CustomerRejectCustomDesignRequest;
import com.capstone.ads.dto.demo_design.DesignerUpdateDescriptionCustomDesignRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.DemoDesignsMapper;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.DemoDesigns;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import com.capstone.ads.model.enums.DemoDesignStatus;
import com.capstone.ads.repository.internal.DemoDesignsRepository;
import com.capstone.ads.service.CustomDesignRequestService;
import com.capstone.ads.service.DemoDesignsService;
import com.capstone.ads.service.S3Service;
import com.capstone.ads.validator.DemoDesignStateValidator;
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
public class DemoDesignsServiceImpl implements DemoDesignsService {
    private final CustomDesignRequestService customDesignRequestService;
    private final S3Service s3Service;
    private final DemoDesignsRepository demoDesignsRepository;
    private final DemoDesignsMapper demoDesignsMapper;
    private final DemoDesignStateValidator demoDesignStateValidator;

    @Override
    @Transactional
    public DemoDesignDTO designerCreateCustomDesign(String customDesignRequestId, DemoDesignCreateRequest request) {
        CustomDesignRequests customDesignRequests = customDesignRequestService.getCustomDesignRequestById(customDesignRequestId);

        DemoDesigns demoDesigns = demoDesignsMapper.mapCreateRequestToEntity(request);

        //Kiểm tra phiên bản nếu có n bản thiết kế thì lúc tạo sẽ là bản thứ n + 1
        int versionNumber = demoDesignsRepository.countByCustomDesignRequests_Id(customDesignRequestId) + 1;

        //Lấy đường dẫn hình ảnh được lưu trữ ở S3
        String customDesignImageKey = uploadCustomDesignImageToS3(customDesignRequestId, request.getCustomDesignImage());

        demoDesigns.setCustomDesignRequests(customDesignRequests);
        demoDesigns.setDemoImage(customDesignImageKey);
        demoDesigns.setStatus(DemoDesignStatus.PENDING);
        demoDesigns.setVersion(versionNumber);
        demoDesigns = demoDesignsRepository.save(demoDesigns);

        //Cập nhật lại trạng thái Request là đã gửi demo
        customDesignRequestService.updateCustomDesignRequestByCustomDesign(customDesignRequestId, versionNumber > 3);

        return demoDesignsMapper.toDTO(demoDesigns);
    }

    @Override
    @Transactional
    public DemoDesignDTO customerApproveCustomDesign(String customDesignId) {
        DemoDesigns demoDesigns = findCustomDesignByIdAndPendingStatus(customDesignId);
        String customDesignRequestId = demoDesigns.getCustomDesignRequests().getId();

        demoDesignStateValidator.validateTransition(demoDesigns.getStatus(), DemoDesignStatus.APPROVED);
        demoDesigns.setStatus(DemoDesignStatus.APPROVED);
        demoDesigns = demoDesignsRepository.save(demoDesigns);

        customDesignRequestService.updateCustomDesignRequestStatus(customDesignRequestId, CustomDesignRequestStatus.WAITING_FULL_PAYMENT);
        return demoDesignsMapper.toDTO(demoDesigns);
    }

    @Override
    @Transactional
    public DemoDesignDTO customerRejectCustomDesign(String customDesignId, CustomerRejectCustomDesignRequest request) {
        DemoDesigns demoDesigns = findCustomDesignByIdAndPendingStatus(customDesignId);
        String customDesignRequestId = demoDesigns.getCustomDesignRequests().getId();

        demoDesignStateValidator.validateTransition(demoDesigns.getStatus(), DemoDesignStatus.REJECTED);
        demoDesignsMapper.updateEntityFromCustomerRequest(request, demoDesigns);
        demoDesigns.setStatus(DemoDesignStatus.REJECTED);
        demoDesigns = demoDesignsRepository.save(demoDesigns);

        customDesignRequestService.updateCustomDesignRequestStatus(customDesignRequestId, CustomDesignRequestStatus.REVISION_REQUESTED);
        return demoDesignsMapper.toDTO(demoDesigns);
    }

    @Override
    @Transactional
    public DemoDesignDTO customerUploadFeedbackImage(String customDesignId, MultipartFile customDesignImage) {
        DemoDesigns demoDesigns = findCustomDesignByIdAndPendingStatus(customDesignId);
        String customDesignRequestId = demoDesigns.getCustomDesignRequests().getId();

        String customDesignImageKey = uploadCustomDesignImageToS3(customDesignRequestId, customDesignImage);
        demoDesigns.setCustomerFeedbackImage(customDesignImageKey);

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
            var designTemplateImagePresigned = s3Service.getPresignedUrl(demoDesigns.getDemoImage(), S3ImageDuration.CUSTOM_DESIGN_DURATION);
            customDesignDTOResponse.setDemoImage(designTemplateImagePresigned);
        }
        return customDesignDTOResponse;
    }

    private String uploadCustomDesignImageToS3(String customDesignRequestId, MultipartFile file) {
        String customDesignImageKey = generateCustomDesignKey(customDesignRequestId);
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        s3Service.uploadSingleFile(customDesignImageKey, file);
        return customDesignImageKey;
    }
}
