package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.PredefinedRole;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestCreateRequest;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomDesignRequestsMapper;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.model.Users;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import com.capstone.ads.repository.internal.CustomDesignRequestsRepository;
import com.capstone.ads.service.*;
import com.capstone.ads.utils.CustomDesignRequestStateValidator;
import com.capstone.ads.utils.CustomerChoiceHistoriesConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomDesignRequestServiceImpl implements CustomDesignRequestService {
    private final S3Service s3Service;
    private final CustomerChoicesService customerChoicesService;
    private final UserService userService;
    private final CustomerDetailService customerDetailService;
    private final CustomDesignRequestsRepository customDesignRequestsRepository;
    private final CustomDesignRequestsMapper customDesignRequestsMapper;
    private final CustomerChoiceHistoriesConverter customerChoiceHistoriesConverter;
    private final CustomDesignRequestStateValidator customDesignRequestStateValidator;

    @Override
    @Transactional
    public CustomDesignRequestDTO createCustomDesignRequest(String customerDetailId, String customerChoicesId, CustomDesignRequestCreateRequest request) {
        customerDetailService.validateCustomerDetailExists(customerDetailId);
        CustomerChoices customerChoices = customerChoicesService.getCustomerChoiceById(customerChoicesId);

        var customDesignRequest = customDesignRequestsMapper.toEntity(request, customerDetailId);
        customDesignRequest.setCustomerChoiceHistories(customerChoiceHistoriesConverter.convertToHistory(customerChoices));
        customDesignRequest = customDesignRequestsRepository.save(customDesignRequest);

        customerChoicesService.hardDeleteCustomerChoice(customerChoicesId);
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
        return customDesignRequestsMapper.toDTO(customerDesignRequest);
    }

    @Override
    @Transactional
    public CustomDesignRequestDTO designerApproveCustomDesignRequest(String customDesignRequestId) {
        CustomDesignRequests customDesignRequest = getCustomDesignRequestById(customDesignRequestId);
        customDesignRequestStateValidator.validateTransition(customDesignRequest.getStatus(), CustomDesignRequestStatus.PROCESSING);

        customDesignRequest.setStatus(CustomDesignRequestStatus.PROCESSING);

        customDesignRequestsRepository.save(customDesignRequest);
        return customDesignRequestsMapper.toDTO(customDesignRequest);
    }

    @Override
    @Transactional
    public CustomDesignRequestDTO designerRejectCustomDesignRequest(String customDesignRequestId) {
        CustomDesignRequests customDesignRequest = getCustomDesignRequestById(customDesignRequestId);
        customDesignRequestStateValidator.validateTransition(customDesignRequest.getStatus(), CustomDesignRequestStatus.DESIGNER_REJECTED);

        customDesignRequest.setStatus(CustomDesignRequestStatus.DESIGNER_REJECTED);
        customDesignRequest.setAssignDesigner(null);

        customDesignRequestsRepository.save(customDesignRequest);
        return customDesignRequestsMapper.toDTO(customDesignRequest);
    }

    @Override
    @Transactional
    public CustomDesignRequestDTO designerUploadFinalDesignImage(String customDesignRequestId, MultipartFile finalDesignImage) {
        CustomDesignRequests customDesignRequest = getCustomDesignRequestById(customDesignRequestId);

        var finalDesignImageUrl = uploadCustomDesignImageToS3(customDesignRequestId, finalDesignImage);
        customDesignRequestStateValidator.validateTransition(customDesignRequest.getStatus(), CustomDesignRequestStatus.COMPLETED);
        customDesignRequest.setFinalDesignImage(finalDesignImageUrl);
        customDesignRequest.setStatus(CustomDesignRequestStatus.COMPLETED);

        return customDesignRequestsMapper.toDTO(customDesignRequest);
    }

    @Override
    public Page<CustomDesignRequestDTO> findCustomerDesignRequestByCustomerDetailId(String customerDetailId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return customDesignRequestsRepository.findByCustomerDetail_Id(customerDetailId, pageable)
                .map(customDesignRequestsMapper::toDTO);
    }

    @Override
    public Page<CustomDesignRequestDTO> findCustomerDetailRequestByAssignDesignerId(String assignDesignerId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return customDesignRequestsRepository.findByAssignDesigner_Id(assignDesignerId, pageable)
                .map(customDesignRequestsMapper::toDTO);
    }

    @Override
    public Page<CustomDesignRequestDTO> findCustomerDetailRequestByStatus(CustomDesignRequestStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return customDesignRequestsRepository.findByStatus(status, pageable)
                .map(customDesignRequestsMapper::toDTO);
    }

    //INTERNAL FUNCTION//

    @Override
    public CustomDesignRequests getCustomDesignRequestById(String customDesignRequestId) {
        return customDesignRequestsRepository.findById(customDesignRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOM_DESIGN_REQUEST_NOT_FOUND));
    }

    @Override
    public void validateCreateCustomDesign(String customDesignRequestId) {
        List<CustomDesignRequestStatus> allowedStatus = Arrays.asList(
                CustomDesignRequestStatus.PROCESSING,
                CustomDesignRequestStatus.REVISION_REQUESTED
        );
        if (!customDesignRequestsRepository.existsByIdAndStatusIn(customDesignRequestId, allowedStatus))
            throw new AppException(ErrorCode.DEMO_DESIGN_IN_WAITING_DECISION_FROM_CUSTOMER);
    }

    @Override
    public void updateCustomDesignRequestStatus(String customDesignRequestId, CustomDesignRequestStatus status) {
        var customDesignRequest = getCustomDesignRequestById(customDesignRequestId);

        customDesignRequestStateValidator.validateTransition(customDesignRequest.getStatus(), status);
        customDesignRequest.setStatus(status);

        customDesignRequestsRepository.save(customDesignRequest);
    }

    @Override
    public void updateCustomDesignRequestByCustomDesign(String customDesignRequestId, boolean isNeedSupport) {
        var customDesignRequest = getCustomDesignRequestById(customDesignRequestId);
        if (isNeedSupport) {
            customDesignRequest.setStatus(CustomDesignRequestStatus.DEMO_SUBMITTED);
            customDesignRequest.setIsNeedSupport(true);
        } else customDesignRequest.setStatus(CustomDesignRequestStatus.DEMO_SUBMITTED);
        customDesignRequestsRepository.save(customDesignRequest);
    }

    @Override
    public void updateCustomDesignRequestApprovedPricing(String customDesignRequestId, Integer totalPrice, Integer depositAmount) {
        var customDesignRequest = getCustomDesignRequestById(customDesignRequestId);

        customDesignRequestStateValidator.validateTransition(customDesignRequest.getStatus(), CustomDesignRequestStatus.APPROVED_PRICING);
        customDesignRequest.setTotalPrice(totalPrice);
        customDesignRequest.setDepositAmount(depositAmount);
        customDesignRequest.setRemainingAmount(totalPrice - depositAmount);
        customDesignRequest = customDesignRequestsRepository.save(customDesignRequest);
        customDesignRequest.setStatus(CustomDesignRequestStatus.APPROVED_PRICING);
    }

    @Override
    public void updateCustomDesignRequestFromWebhookResult(CustomDesignRequests customDesignRequests, boolean isDeposit) {
        if (isDeposit) customDesignRequests.setStatus(CustomDesignRequestStatus.DEPOSITED);
        else customDesignRequests.setStatus(CustomDesignRequestStatus.FULLY_PAID);
        customDesignRequestsRepository.save(customDesignRequests);
    }

    private String generateCustomDesignRequestKey(String customDesignRequestId) {
        return String.format("custom-design/%s/final", customDesignRequestId);
    }

    private String uploadCustomDesignImageToS3(String customDesignRequestId, MultipartFile file) {
        String customDesignImageKey = generateCustomDesignRequestKey(customDesignRequestId);
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        s3Service.uploadSingleFile(customDesignImageKey, file);
        return customDesignImageKey;
    }
}
