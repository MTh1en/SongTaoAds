package com.capstone.ads.service;

import com.capstone.ads.dto.custom_design_request.CustomDesignRequestCreateRequest;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestDTO;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestFinalDesignRequest;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import org.springframework.data.domain.Page;

public interface CustomDesignRequestService {
    CustomDesignRequestDTO createCustomDesignRequest(String customerDetailId, CustomDesignRequestCreateRequest request);

    CustomDesignRequestDTO assignDesignerToCustomerRequest(String customDesignRequestId, String designerId);

    CustomDesignRequestDTO designerApproveCustomDesignRequest(String customDesignRequestId);

    CustomDesignRequestDTO designerRejectCustomDesignRequest(String customDesignRequestId);

    CustomDesignRequestDTO designerUploadFinalDesignImage(String customDesignRequestId, CustomDesignRequestFinalDesignRequest request);

    Page<CustomDesignRequestDTO> findCustomDesignRequestByCustomerDetailId(String customerDetailId, int page, int size);

    Page<CustomDesignRequestDTO> findCustomDesignRequestByAssignDesignerId(String assignDesignerId, int page, int size);

    Page<CustomDesignRequestDTO> findAllCustomDesignRequestNeedSupport(int page, int size);

    Page<CustomDesignRequestDTO> findCustomerDesignRequest(CustomDesignRequestStatus status, int page, int size);

    Page<CustomDesignRequestDTO> searchCustomDesignRequest(String keyword, int page, int size);

    Page<CustomDesignRequestDTO> searchCustomDesignRequestAssigned(String keyword, int page, int size);

    //INTERNAL FUNCTION
    CustomDesignRequests getCustomDesignRequestById(String customDesignRequestId);
}
