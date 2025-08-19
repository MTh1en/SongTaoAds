package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.NotificationMessage;
import com.capstone.ads.constaint.PredefinedRole;
import com.capstone.ads.constaint.S3ImageKeyFormat;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestCreateRequest;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestDTO;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestFinalDesignRequest;
import com.capstone.ads.event.*;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomDesignRequestsMapper;
import com.capstone.ads.model.*;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import com.capstone.ads.model.enums.FileTypeEnum;
import com.capstone.ads.repository.internal.CustomDesignRequestsRepository;
import com.capstone.ads.service.*;
import com.capstone.ads.utils.KeyGenerator;
import com.capstone.ads.utils.SecurityContextUtils;
import com.capstone.ads.validator.CustomDesignRequestStateValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomDesignRequestServiceImpl implements CustomDesignRequestService {
    FileDataService fileDataService;
    UserService userService;
    CustomerDetailService customerDetailService;
    CustomDesignRequestsRepository customDesignRequestsRepository;
    CustomDesignRequestsMapper customDesignRequestsMapper;
    CustomDesignRequestStateValidator customDesignRequestStateValidator;
    SecurityContextUtils securityContextUtils;
    ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public CustomDesignRequestDTO createCustomDesignRequest(String customerDetailId, CustomDesignRequestCreateRequest request) {
        CustomerDetail customerDetail = customerDetailService.getCustomerDetailById(customerDetailId);

        var customDesignRequest = customDesignRequestsMapper.mapCreateRequestToEntity(request);
        customDesignRequest.setCode(KeyGenerator.generateCustomDesignRequest());
        customDesignRequest.setIsNeedSupport(false);
        customDesignRequest.setCustomerDetail(customerDetail);
        customDesignRequest.setStatus(CustomDesignRequestStatus.PENDING);
        customDesignRequest = customDesignRequestsRepository.save(customDesignRequest);
        return customDesignRequestsMapper.toDTO(customDesignRequest);
    }

    @Override
    @Transactional
    public CustomDesignRequestDTO assignDesignerToCustomerRequest(String customDesignRequestId, String designerId) {
        Users designer = userService.getUsersByIdAndIsActiveAndRoleName(designerId, true, PredefinedRole.DESIGNER_ROLE);
        List<CustomDesignRequestStatus> allowedStatus = Arrays.asList(
                CustomDesignRequestStatus.DEPOSITED,
                CustomDesignRequestStatus.DESIGNER_REJECTED);

        var customerDesignRequest = customDesignRequestsRepository.findByIdAndStatusIn(customDesignRequestId, allowedStatus)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOM_DESIGN_REQUEST_DEPOSITED_NOT_FOUND));

        customerDesignRequest.setAssignDesigner(designer);
        customerDesignRequest.setStatus(CustomDesignRequestStatus.ASSIGNED_DESIGNER);
        customerDesignRequest = customDesignRequestsRepository.save(customerDesignRequest);

        eventPublisher.publishEvent(new UserNotificationEvent(
                this,
                designerId,
                String.format(NotificationMessage.ASSIGN_DESIGNER, customerDesignRequest.getCode())
        ));

        return customDesignRequestsMapper.toDTO(customerDesignRequest);
    }

    @Override
    @Transactional
    public CustomDesignRequestDTO designerApproveCustomDesignRequest(String customDesignRequestId) {
        CustomDesignRequests customDesignRequest = getCustomDesignRequestById(customDesignRequestId);
        customDesignRequestStateValidator.validateTransition(customDesignRequest.getStatus(), CustomDesignRequestStatus.PROCESSING);

        customDesignRequest.setStatus(CustomDesignRequestStatus.PROCESSING);

        customDesignRequestsRepository.save(customDesignRequest);

        eventPublisher.publishEvent(new UserNotificationEvent(
                this,
                customDesignRequest.getCustomerDetail().getUsers().getId(),
                String.format(NotificationMessage.DEFAULT,
                        customDesignRequest.getCode(), CustomDesignRequestStatus.PROCESSING.getMessage())
        ));

        return customDesignRequestsMapper.toDTO(customDesignRequest);
    }

    @Override
    @Transactional
    public CustomDesignRequestDTO designerRejectCustomDesignRequest(String customDesignRequestId) {
        var user = securityContextUtils.getCurrentUser();
        CustomDesignRequests customDesignRequest = getCustomDesignRequestById(customDesignRequestId);
        customDesignRequestStateValidator.validateTransition(customDesignRequest.getStatus(), CustomDesignRequestStatus.DESIGNER_REJECTED);

        customDesignRequest.setStatus(CustomDesignRequestStatus.DESIGNER_REJECTED);
        customDesignRequest.setAssignDesigner(null);

        customDesignRequestsRepository.save(customDesignRequest);

        eventPublisher.publishEvent(new RoleNotificationEvent(
                this,
                PredefinedRole.SALE_ROLE,
                String.format(NotificationMessage.DESIGNER_REJECTED,
                        customDesignRequest.getCode(), user.getFullName())
        ));
        return customDesignRequestsMapper.toDTO(customDesignRequest);
    }

    @Override
    @Transactional
    public CustomDesignRequestDTO designerUploadFinalDesignImage(String customDesignRequestId, CustomDesignRequestFinalDesignRequest request) {
        CustomDesignRequests customDesignRequest = getCustomDesignRequestById(customDesignRequestId);
        customDesignRequestStateValidator.validateTransition(
                customDesignRequest.getStatus(),
                CustomDesignRequestStatus.COMPLETED
        );

        var finalDesignImageUrl = uploadCustomDesignRequestImageToS3(customDesignRequestId, request.getFinalDesignImage());
        customDesignRequest.setFinalDesignImage(finalDesignImageUrl);
        customDesignRequest.setStatus(CustomDesignRequestStatus.COMPLETED);
        customDesignRequestsRepository.save(customDesignRequest);

        if (request.getSubFinalDesignImages() != null && !request.getSubFinalDesignImages().isEmpty()) {
            fileDataService.uploadMultipleFiles(
                    request.getSubFinalDesignImages(),
                    FileTypeEnum.CUSTOM_DESIGN_REQUEST,
                    customDesignRequest,
                    FileData::setCustomDesignRequests,
                    (id, size) -> generateCustomDesignRequestSubImagesKey(customDesignRequestId, size)
            );
        }

        eventPublisher.publishEvent(new CustomDesignRequestCompletedEvent(
                this,
                customDesignRequestId
        ));

        eventPublisher.publishEvent(new UserNotificationEvent(
                this,
                customDesignRequest.getCustomerDetail().getUsers().getId(),
                String.format(NotificationMessage.DEFAULT, customDesignRequest.getCode(), CustomDesignRequestStatus.COMPLETED.getMessage())
        ));

        return customDesignRequestsMapper.toDTO(customDesignRequest);
    }

    @Override
    public Page<CustomDesignRequestDTO> findCustomDesignRequestByCustomerDetailId(String customerDetailId, int page, int size) {
        Sort sort = Sort.by("updatedAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return customDesignRequestsRepository.findByCustomerDetail_Id(customerDetailId, pageable)
                .map(customDesignRequestsMapper::toDTO);
    }

    @Override
    public Page<CustomDesignRequestDTO> findCustomDesignRequestByAssignDesignerId(String assignDesignerId, int page, int size) {
        Sort sort = Sort.by("updatedAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return customDesignRequestsRepository.findByAssignDesigner_Id(assignDesignerId, pageable)
                .map(customDesignRequestsMapper::toDTO);
    }

    @Override
    public Page<CustomDesignRequestDTO> findCustomDesignRequestByStatus(CustomDesignRequestStatus status, int page, int size) {
        Sort sort = Sort.by("updatedAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return customDesignRequestsRepository.findByStatus(status, pageable)
                .map(customDesignRequestsMapper::toDTO);
    }

    @Override
    public Page<CustomDesignRequestDTO> findAllCustomDesignRequestNeedSupport(int page, int size) {
        Sort sort = Sort.by("updatedAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return customDesignRequestsRepository.findByIsNeedSupport(true, pageable)
                .map(customDesignRequestsMapper::toDTO);
    }

    @Override
    public Page<CustomDesignRequestDTO> findAllCustomerDesignRequest(int page, int size) {
        Sort sort = Sort.by("updatedAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return customDesignRequestsRepository.findAll(pageable)
                .map(customDesignRequestsMapper::toDTO);
    }

    //INTERNAL FUNCTION//

    @Override
    public CustomDesignRequests getCustomDesignRequestById(String customDesignRequestId) {
        return customDesignRequestsRepository.findById(customDesignRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOM_DESIGN_REQUEST_NOT_FOUND));
    }

    // UPLOAD IMAGE //
    private String generateCustomDesignRequestKey(String customDesignRequestId) {
        return String.format(S3ImageKeyFormat.FINAL_CUSTOM_DESIGN, customDesignRequestId);
    }

    private List<String> generateCustomDesignRequestSubImagesKey(String customDesignRequestId, Integer amountKey) {
        List<String> keys = new ArrayList<>();
        IntStream.range(0, amountKey)
                .forEach(i -> keys.add(String.format(S3ImageKeyFormat.FINAL_CUSTOM_DESIGN_SUB_IMAGE, customDesignRequestId, UUID.randomUUID())));
        return keys;
    }

    private String uploadCustomDesignRequestImageToS3(String customDesignRequestId, MultipartFile file) {
        String customDesignImageKey = generateCustomDesignRequestKey(customDesignRequestId);
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        fileDataService.uploadSingleFile(customDesignImageKey, file);
        return customDesignImageKey;
    }

    // HANDLE EVENT //

    @Async("delegatingSecurityContextAsyncTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCustomDesignPaymentEvent(CustomDesignPaymentEvent event) {
        CustomDesignRequests customDesignRequests = getCustomDesignRequestById(event.getCustomDesignRequestId());

        if (event.getIsDeposit()) {
            customDesignRequestStateValidator.validateTransition(customDesignRequests.getStatus(), CustomDesignRequestStatus.DEPOSITED);
            customDesignRequests.setStatus(CustomDesignRequestStatus.DEPOSITED);

            eventPublisher.publishEvent(new RoleNotificationEvent(
                    this,
                    PredefinedRole.SALE_ROLE,
                    String.format(NotificationMessage.CUSTOM_DESIGN_REQUEST_DEPOSITED, customDesignRequests.getCode())
            ));

        } else {
            customDesignRequestStateValidator.validateTransition(customDesignRequests.getStatus(), CustomDesignRequestStatus.FULLY_PAID);
            customDesignRequests.setStatus(CustomDesignRequestStatus.FULLY_PAID);

            eventPublisher.publishEvent(new UserNotificationEvent(
                    this,
                    customDesignRequests.getAssignDesigner().getId(),
                    String.format(NotificationMessage.CUSTOM_DESIGN_REQUEST_FULLY_PAID, customDesignRequests.getCode())
            ));
        }
        customDesignRequestsRepository.save(customDesignRequests);
    }

    @Async("delegatingSecurityContextAsyncTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCustomDesignRequestChangeStatusEvent(CustomDesignRequestChangeStatusEvent event) {
        CustomDesignRequests customDesignRequests = getCustomDesignRequestById(event.getCustomDesignRequestId());
        customDesignRequests.setStatus(event.getStatus());
        customDesignRequestsRepository.save(customDesignRequests);

        eventPublisher.publishEvent(new UserNotificationEvent(
                this,
                customDesignRequests.getCustomerDetail().getUsers().getId(),
                String.format(NotificationMessage.DEFAULT,
                        customDesignRequests.getCode(),
                        event.getStatus().getMessage())
        ));
    }

    @Async("delegatingSecurityContextAsyncTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePriceProposalApprovedEvent(PriceProposalApprovedEvent event) {
        log.info("custom design request");
        var customDesignRequest = getCustomDesignRequestById(event.getCustomDesignRequestId());

        customDesignRequest.setTotalPrice(event.getTotalPrice());
        customDesignRequest.setDepositAmount(event.getDepositAmount());
        customDesignRequest.setRemainingAmount(event.getRemainingAmount());
        customDesignRequest.setStatus(CustomDesignRequestStatus.APPROVED_PRICING);
        customDesignRequestsRepository.save(customDesignRequest);

        eventPublisher.publishEvent(new CustomDesignRequestPricingApprovedEvent(
                this,
                customDesignRequest.getId(), // Hoặc truyền lại các thông tin cần thiết
                event.getTotalPrice(),
                event.getDepositAmount(),
                event.getRemainingAmount()
        ));

        eventPublisher.publishEvent(new UserNotificationEvent(
                this,
                customDesignRequest.getCustomerDetail().getUsers().getId(),
                String.format(NotificationMessage.DEFAULT,
                        customDesignRequest.getCode(),
                        CustomDesignRequestStatus.APPROVED_PRICING.getMessage())
        ));
    }

    @Async("delegatingSecurityContextAsyncTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleDemoDesignCreateEvent(DemoDesignCreateEvent event) {
        var customDesignRequest = getCustomDesignRequestById(event.getCustomDesignRequestId());
        if (event.isNeedSupport()) {
            customDesignRequest.setStatus(CustomDesignRequestStatus.DEMO_SUBMITTED);
            customDesignRequest.setIsNeedSupport(true);
        } else customDesignRequest.setStatus(CustomDesignRequestStatus.DEMO_SUBMITTED);
        customDesignRequestsRepository.save(customDesignRequest);

        eventPublisher.publishEvent(new UserNotificationEvent(
                this,
                customDesignRequest.getCustomerDetail().getUsers().getId(),
                String.format(NotificationMessage.DEFAULT,
                        customDesignRequest.getCode(),
                        CustomDesignRequestStatus.DEMO_SUBMITTED.getMessage())
        ));
    }

    @Async("delegatingSecurityContextAsyncTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleDemoDesignApprovedEvent(DemoDesignApprovedEvent event) {
        var customDesignRequest = getCustomDesignRequestById(event.getCustomDesignRequestId());
        customDesignRequest.setStatus(CustomDesignRequestStatus.WAITING_FULL_PAYMENT);
        customDesignRequestsRepository.save(customDesignRequest);

        eventPublisher.publishEvent(new CustomDesignRequestDemoSubmittedEvent(
                this,
                customDesignRequest.getId()
        ));

        eventPublisher.publishEvent(new UserNotificationEvent(
                this,
                customDesignRequest.getCustomerDetail().getUsers().getId(),
                String.format(NotificationMessage.DEFAULT,
                        customDesignRequest.getCode(),
                        CustomDesignRequestStatus.WAITING_FULL_PAYMENT.getMessage())
        ));
    }
}
