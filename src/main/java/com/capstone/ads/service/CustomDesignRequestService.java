package com.capstone.ads.service;

import com.capstone.ads.dto.customdesignrequest.CustomDesignRequestCreateRequest;
import com.capstone.ads.dto.customdesignrequest.CustomDesignRequestDTO;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomDesignRequestService {
    CustomDesignRequestDTO createCustomDesignRequest(String customerDetailId, String customerChoiceId,
                                                     CustomDesignRequestCreateRequest request);

    CustomDesignRequestDTO assignDesignerToCustomerRequest(String customDesignRequestId, String designerId);

    Page<CustomDesignRequestDTO> findCustomerDesignRequestByCustomerDetailId(String customerDetailId, int page, int size);

    Page<CustomDesignRequestDTO> findCustomerDetailRequestByAssignDesignerId(String assignDesignerId, int page, int size);

    Page<CustomDesignRequestDTO> findCustomerDetailRequestByStatus(CustomDesignRequestStatus status, int page, int size);

    CustomDesignRequestDTO changeStatusCustomDesignRequest(String customDesignRequestId, CustomDesignRequestStatus status);
}
