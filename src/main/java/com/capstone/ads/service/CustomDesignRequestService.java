package com.capstone.ads.service;

import com.capstone.ads.dto.custom_design_request.CustomDesignRequestCreateRequest;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestDTO;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface CustomDesignRequestService {
    CustomDesignRequestDTO createCustomDesignRequest(String customerDetailId, String customerChoiceId,
                                                     CustomDesignRequestCreateRequest request);

    CustomDesignRequestDTO assignDesignerToCustomerRequest(String customDesignRequestId, String designerId);

    CustomDesignRequestDTO designerApproveCustomDesignRequest(String customDesignRequestId);

    CustomDesignRequestDTO designerRejectCustomDesignRequest(String customDesignRequestId);

    CustomDesignRequestDTO designerUploadFinalDesignImage(String customDesignRequestId, MultipartFile finalDesignImage);

    Page<CustomDesignRequestDTO> findCustomerDesignRequestByCustomerDetailId(String customerDetailId, int page, int size);

    Page<CustomDesignRequestDTO> findCustomerDetailRequestByAssignDesignerId(String assignDesignerId, int page, int size);

    Page<CustomDesignRequestDTO> findCustomerDetailRequestByStatus(CustomDesignRequestStatus status, int page, int size);

    //INTERNAL FUNCTION
    CustomDesignRequests getCustomDesignRequestById(String customDesignRequestId);

    void updateCustomDesignRequestStatus(String customDesignRequestId, CustomDesignRequestStatus status);

    void updateCustomDesignRequestByCustomDesign(String customDesignRequestId, boolean isNeedSupport);

    void updateCustomDesignRequestApprovedPricing(String customDesignRequestId, Long totalPrice, Long depositAmount);

    void updateCustomDesignRequestFromWebhookResult(CustomDesignRequests customDesignRequests, boolean isDeposit);
}
