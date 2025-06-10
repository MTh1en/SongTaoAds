package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.PredefinedRole;
import com.capstone.ads.dto.customdesignrequest.CustomDesignRequestCreateRequest;
import com.capstone.ads.dto.customdesignrequest.CustomDesignRequestDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomDesignRequestsMapper;
import com.capstone.ads.model.Users;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import com.capstone.ads.repository.internal.CustomDesignRequestsRepository;
import com.capstone.ads.repository.internal.CustomerChoicesRepository;
import com.capstone.ads.repository.internal.CustomerDetailRepository;
import com.capstone.ads.repository.internal.UsersRepository;
import com.capstone.ads.service.CustomDesignRequestService;
import com.capstone.ads.utils.CustomDesignRequestStateValidator;
import com.capstone.ads.utils.CustomerChoiceHistoriesConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomDesignRequestServiceImpl implements CustomDesignRequestService {
    private final CustomDesignRequestsRepository customDesignRequestsRepository;
    private final CustomerChoicesRepository customerChoicesRepository;
    private final UsersRepository usersRepository;
    private final CustomerDetailRepository customerDetailRepository;
    private final CustomDesignRequestsMapper customDesignRequestsMapper;
    private final CustomerChoiceHistoriesConverter customerChoiceHistoriesConverter;
    private final CustomDesignRequestStateValidator customDesignRequestStateValidator;

    @Override
    @Transactional
    public CustomDesignRequestDTO createCustomDesignRequest(String customerDetailId, String customerChoicesId, CustomDesignRequestCreateRequest request) {
        if (!customerDetailRepository.existsById(customerDetailId)) {
            throw new AppException(ErrorCode.CUSTOMER_DETAIL_NOT_FOUND);
        }
        var customerChoices = customerChoicesRepository.findById(customerChoicesId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));

        var customDesignRequest = customDesignRequestsMapper.toEntity(request, customerDetailId);
        customDesignRequest.setCustomerChoiceHistories(customerChoiceHistoriesConverter.convertToHistory(customerChoices));
        customDesignRequest = customDesignRequestsRepository.save(customDesignRequest);
        customerChoicesRepository.deleteById(customerChoicesId);
        return customDesignRequestsMapper.toDTO(customDesignRequest);
    }

    @Override
    @Transactional
    public CustomDesignRequestDTO assignDesignerToCustomerRequest(String customDesignRequestId, String designerId) {
        List<CustomDesignRequestStatus> allowedStatuses = Arrays.asList(
                CustomDesignRequestStatus.PENDING,
                CustomDesignRequestStatus.APPROVED
        );
        var customerDesignRequest = customDesignRequestsRepository.findByIdAndStatusIn(customDesignRequestId, allowedStatuses)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOM_DESIGN_REQUEST_PENDING_NOT_FOUND));
        Users designer = usersRepository.findByIdAndIsActiveAndRoles_Name(designerId, true, PredefinedRole.DESIGNER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.DESIGNER_NOT_FOUND));

        customerDesignRequest.setAssignDesigner(designer);
        customerDesignRequest.setStatus(CustomDesignRequestStatus.APPROVED);
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
}
