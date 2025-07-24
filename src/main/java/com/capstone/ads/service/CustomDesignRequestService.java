package com.capstone.ads.service;

import com.capstone.ads.dto.custom_design_request.CustomDesignRequestCreateRequest;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestDTO;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestFinalDesignRequest;
import com.capstone.ads.dto.file.FileDataDTO;
import com.capstone.ads.dto.file.UploadMultipleFileRequest;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomDesignRequestService {
    CustomDesignRequestDTO createCustomDesignRequest(String customerDetailId, CustomDesignRequestCreateRequest request);

    CustomDesignRequestDTO assignDesignerToCustomerRequest(String customDesignRequestId, String designerId);

    CustomDesignRequestDTO designerApproveCustomDesignRequest(String customDesignRequestId);

    CustomDesignRequestDTO designerRejectCustomDesignRequest(String customDesignRequestId);

    CustomDesignRequestDTO designerUploadFinalDesignImage(String customDesignRequestId, CustomDesignRequestFinalDesignRequest request);

    Page<CustomDesignRequestDTO> findCustomerDesignRequestByCustomerDetailId(String customerDetailId, int page, int size);

    Page<CustomDesignRequestDTO> findCustomerDetailRequestByAssignDesignerId(String assignDesignerId, int page, int size);

    Page<CustomDesignRequestDTO> findCustomerDetailRequestByStatus(CustomDesignRequestStatus status, int page, int size);

    //INTERNAL FUNCTION
    CustomDesignRequests getCustomDesignRequestById(String customDesignRequestId);
}
