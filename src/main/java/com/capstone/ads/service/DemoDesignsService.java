package com.capstone.ads.service;

import com.capstone.ads.dto.demo_design.DemoDesignCreateRequest;
import com.capstone.ads.dto.demo_design.DemoDesignDTO;
import com.capstone.ads.dto.demo_design.CustomerRejectCustomDesignRequest;
import com.capstone.ads.dto.demo_design.DesignerUpdateDescriptionCustomDesignRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface DemoDesignsService {
    DemoDesignDTO designerCreateCustomDesign(String customDesignRequestId, DemoDesignCreateRequest request);

    DemoDesignDTO customerApproveCustomDesign(String customDesignId);

    DemoDesignDTO customerRejectCustomDesign(String customDesignId, CustomerRejectCustomDesignRequest request);

    DemoDesignDTO customerUploadFeedbackImage(String customDesignId, MultipartFile customDesignImage);

    DemoDesignDTO designerUpdateDescriptionCustomDesign(String customDesignId, DesignerUpdateDescriptionCustomDesignRequest request);

    DemoDesignDTO designerUploadImage(String customDesignId, MultipartFile file);

    Page<DemoDesignDTO> findCustomDesignByCustomDesignRequest(String customDesignRequestId, int page, int size);

    void hardDeleteCustomDesign(String customDesignId);
}
