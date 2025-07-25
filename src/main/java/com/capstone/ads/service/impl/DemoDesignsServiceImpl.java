package com.capstone.ads.service.impl;

import com.capstone.ads.dto.demo_design.DemoDesignCreateRequest;
import com.capstone.ads.dto.demo_design.DemoDesignDTO;
import com.capstone.ads.dto.demo_design.CustomerRejectCustomDesignRequest;
import com.capstone.ads.dto.demo_design.DesignerUpdateDescriptionCustomDesignRequest;
import com.capstone.ads.dto.file.FileDataDTO;
import com.capstone.ads.dto.file.UploadMultipleFileRequest;
import com.capstone.ads.event.CustomDesignRequestChangeStatusEvent;
import com.capstone.ads.event.DemoDesignApprovedEvent;
import com.capstone.ads.event.DemoDesignCreateEvent;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.DemoDesignsMapper;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.DemoDesigns;
import com.capstone.ads.model.FileData;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import com.capstone.ads.model.enums.DemoDesignStatus;
import com.capstone.ads.model.enums.FileTypeEnum;
import com.capstone.ads.repository.internal.DemoDesignsRepository;
import com.capstone.ads.service.CustomDesignRequestService;
import com.capstone.ads.service.DemoDesignsService;
import com.capstone.ads.service.FileDataService;
import com.capstone.ads.validator.CustomDesignRequestStateValidator;
import com.capstone.ads.validator.DemoDesignStateValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DemoDesignsServiceImpl implements DemoDesignsService {
    CustomDesignRequestService customDesignRequestService;
    FileDataService fileDataService;
    DemoDesignsRepository demoDesignsRepository;
    DemoDesignsMapper demoDesignsMapper;
    DemoDesignStateValidator demoDesignStateValidator;
    CustomDesignRequestStateValidator customDesignRequestStateValidator;
    ApplicationEventPublisher eventPublisher;

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

        DemoDesigns finalDemoDesigns = demoDesigns;
        if (request.getSubCustomDesignImage() != null && !request.getSubCustomDesignImage().isEmpty()) {
            fileDataService.uploadMultipleFiles(
                    request.getSubCustomDesignImage(),
                    FileTypeEnum.DEMO_DESIGN,
                    demoDesigns,
                    FileData::setDemoDesigns,
                    (id, size) -> generateDemoDesignSumImageKey(finalDemoDesigns.getCustomDesignRequests().getId(), size)
            );
        }

        customDesignRequestStateValidator.validateTransition(
                demoDesigns.getCustomDesignRequests().getStatus(),
                CustomDesignRequestStatus.DEMO_SUBMITTED
        );

        eventPublisher.publishEvent(new DemoDesignCreateEvent(
                this,
                customDesignRequestId,
                versionNumber > 3
        ));

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

        customDesignRequestStateValidator.validateTransition(
                demoDesigns.getCustomDesignRequests().getStatus(),
                CustomDesignRequestStatus.WAITING_FULL_PAYMENT
        );
        eventPublisher.publishEvent(new DemoDesignApprovedEvent(
                this,
                customDesignRequestId
        ));

        return demoDesignsMapper.toDTO(demoDesigns);
    }

    @Override
    @Transactional
    public DemoDesignDTO customerRejectCustomDesign(String customDesignId, CustomerRejectCustomDesignRequest request) {
        DemoDesigns demoDesigns = findCustomDesignByIdAndPendingStatus(customDesignId);
        String customDesignRequestId = demoDesigns.getCustomDesignRequests().getId();

        demoDesignStateValidator.validateTransition(demoDesigns.getStatus(), DemoDesignStatus.REJECTED);

        if (request.getFeedbackImage() != null && !request.getFeedbackImage().isEmpty()) {
            String customDesignImageKey = uploadCustomDesignImageToS3(customDesignRequestId, request.getFeedbackImage());
            demoDesigns.setCustomerFeedbackImage(customDesignImageKey);
        }
        demoDesignsMapper.updateEntityFromCustomerRequest(request, demoDesigns);
        demoDesigns.setStatus(DemoDesignStatus.REJECTED);
        demoDesigns = demoDesignsRepository.save(demoDesigns);

        customDesignRequestStateValidator.validateTransition(
                demoDesigns.getCustomDesignRequests().getStatus(),
                CustomDesignRequestStatus.REVISION_REQUESTED
        );

        eventPublisher.publishEvent(new CustomDesignRequestChangeStatusEvent(
                this,
                customDesignRequestId,
                CustomDesignRequestStatus.REVISION_REQUESTED
        ));

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
        fileDataService.hardDeleteFileDataByImageUrl(demoDesigns.getDemoImage());

        String customDesignImageKey = uploadCustomDesignImageToS3(customDesignRequestId, file);
        demoDesigns.setDemoImage(customDesignImageKey);

        demoDesigns = demoDesignsRepository.save(demoDesigns);
        return demoDesignsMapper.toDTO(demoDesigns);
    }

    @Override
    public List<FileDataDTO> uploadDemoDesignSubImages(String demoDesignId, UploadMultipleFileRequest request) {
        DemoDesigns demoDesigns = getDemoDesignsById(demoDesignId);
        return fileDataService.uploadMultipleFiles(
                request.getFiles(),
                FileTypeEnum.DEMO_DESIGN,
                demoDesigns,
                FileData::setDemoDesigns,
                (id, size) -> generateDemoDesignSumImageKey(demoDesigns.getCustomDesignRequests().getId(), size)
        );
    }

    @Override
    public Page<DemoDesignDTO> findCustomDesignByCustomDesignRequest(String customDesignRequestId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return demoDesignsRepository.findByCustomDesignRequests_Id(customDesignRequestId, pageable)
                .map(demoDesignsMapper::toDTO);
    }

    @Override
    @Transactional
    public void hardDeleteCustomDesign(String customDesignId) {
        if (!demoDesignsRepository.existsById(customDesignId)) {
            throw new AppException(ErrorCode.CUSTOM_DESIGN_NOT_FOUND);
        }
        demoDesignsRepository.deleteById(customDesignId);
    }

    @Override
    public DemoDesigns getDemoDesignsById(String customDesignId) {
        return demoDesignsRepository.findById(customDesignId)
                .orElseThrow(() -> new AppException(ErrorCode.DEMO_DESIGN_NOT_FOUND));
    }

    private DemoDesigns findCustomDesignByIdAndPendingStatus(String customDesignId) {
        return demoDesignsRepository.findByIdAndStatus(customDesignId, DemoDesignStatus.PENDING)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOM_DESIGN_NOT_FOUND));
    }

    private String generateCustomDesignKey(String customDesignRequestId) {
        return String.format("custom-design/%s/%s", customDesignRequestId, UUID.randomUUID());
    }

    private List<String> generateDemoDesignSumImageKey(String customDesignRequestId, Integer amountKey) {
        List<String> keys = new ArrayList<>();
        IntStream.range(0, amountKey)
                .forEach(i -> keys.add(String.format("custom-design/%s/sub-demo/%s",
                        customDesignRequestId, UUID.randomUUID())));
        return keys;
    }

    private String uploadCustomDesignImageToS3(String customDesignRequestId, MultipartFile file) {
        String customDesignImageKey = generateCustomDesignKey(customDesignRequestId);
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        fileDataService.uploadSingleFile(customDesignImageKey, file);
        return customDesignImageKey;
    }
}
