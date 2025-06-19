package com.capstone.ads.service;

import com.capstone.ads.dto.custom_design_request.CustomDesignRequestCreateRequest;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestDTO;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import org.springframework.data.domain.Page;

public interface CustomDesignRequestService {
    CustomDesignRequestDTO createCustomDesignRequest(String customerDetailId, String customerChoiceId,
                                                     CustomDesignRequestCreateRequest request);

    CustomDesignRequestDTO assignDesignerToCustomerRequest(String customDesignRequestId, String designerId);

    Page<CustomDesignRequestDTO> findCustomerDesignRequestByCustomerDetailId(String customerDetailId, int page, int size);

    Page<CustomDesignRequestDTO> findCustomerDetailRequestByAssignDesignerId(String assignDesignerId, int page, int size);

    Page<CustomDesignRequestDTO> findCustomerDetailRequestByStatus(CustomDesignRequestStatus status, int page, int size);

    CustomDesignRequestDTO changeStatusCustomDesignRequest(String customDesignRequestId, CustomDesignRequestStatus status);

    //INTERNAL FUNCTION
    CustomDesignRequests getCustomDesignRequestById(String customDesignRequestId);

    void updateCustomDesignRequestStatus(String customDesignRequestId, CustomDesignRequestStatus status);

    void updateCustomDesignRequestApprovedPricing(String customDesignRequestId, Integer totalPrice, Integer depositAmount);

    void updateCustomDesignRequestFromWebhookResult(CustomDesignRequests customDesignRequests, boolean isDeposit);
}
