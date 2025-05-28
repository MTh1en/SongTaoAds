package com.capstone.ads.service;

import com.capstone.ads.dto.customdesignrequest.CustomDesignRequestCreateRequest;
import com.capstone.ads.dto.customdesignrequest.CustomDesignRequestDTO;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;

import java.util.List;

public interface CustomDesignRequestService {
    CustomDesignRequestDTO createCustomDesignRequest(String customerDetailId, String customerChoiceId,
                                                     CustomDesignRequestCreateRequest request);

    CustomDesignRequestDTO assignDesignerToCustomerRequest(String customDesignRequestId, String designerId);

    List<CustomDesignRequestDTO> findCustomerDesignRequestByCustomerDetailId(String customerDetailId);

    List<CustomDesignRequestDTO> findCustomerDetailRequestByAssignDesignerId(String assignDesignerId);

    List<CustomDesignRequestDTO> findCustomerDetailRequestByStatus(CustomDesignRequestStatus status);

    CustomDesignRequestDTO changeStatusCustomDesignRequest(String customDesignRequestId, CustomDesignRequestStatus status);
}
