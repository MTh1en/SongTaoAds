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
import com.capstone.ads.service.CustomDesignRequestService;
import com.capstone.ads.service.CustomerChoicesService;
import com.capstone.ads.service.CustomerDetailService;
import com.capstone.ads.service.UserService;
import com.capstone.ads.utils.CustomDesignRequestStateValidator;
import com.capstone.ads.utils.CustomerChoiceHistoriesConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomDesignRequestServiceImpl implements CustomDesignRequestService {
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
        var customerDesignRequest = customDesignRequestsRepository.findByIdAndStatus(customDesignRequestId, CustomDesignRequestStatus.DEPOSITED)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOM_DESIGN_REQUEST_DEPOSITED_NOT_FOUND));

        customerDesignRequest.setAssignDesigner(designer);
        customerDesignRequest.setStatus(CustomDesignRequestStatus.ASSIGNED_DESIGNER);
        customerDesignRequest = customDesignRequestsRepository.save(customerDesignRequest);
        return customDesignRequestsMapper.toDTO(customerDesignRequest);
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

    @Override
    @Transactional
    public CustomDesignRequestDTO changeStatusCustomDesignRequest(String customDesignRequestId, CustomDesignRequestStatus newStatus) {
        var customDesignRequest = customDesignRequestsRepository.findById(customDesignRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOM_DESIGN_REQUEST_NOT_FOUND));

        customDesignRequestStateValidator.validateTransition(
                customDesignRequest.getStatus(),
                newStatus);

        customDesignRequest.setStatus(newStatus);
        customDesignRequest = customDesignRequestsRepository.save(customDesignRequest);
        return customDesignRequestsMapper.toDTO(customDesignRequest);
    }


    //INTERNAL FUNCTION//

    @Override
    public CustomDesignRequests getCustomDesignRequestById(String customDesignRequestId) {
        return customDesignRequestsRepository.findById(customDesignRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOM_DESIGN_REQUEST_NOT_FOUND));
    }

    @Override
    public void updateCustomDesignRequestStatus(String customDesignRequestId, CustomDesignRequestStatus status) {
        var customDesignRequest = getCustomDesignRequestById(customDesignRequestId);

        customDesignRequestStateValidator.validateTransition(customDesignRequest.getStatus(), status);
        customDesignRequest.setStatus(status);

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
}
