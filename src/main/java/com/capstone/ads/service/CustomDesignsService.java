package com.capstone.ads.service;

import com.capstone.ads.dto.customdesign.CustomDesignCreateRequest;
import com.capstone.ads.dto.customdesign.CustomDesignDTO;
import com.capstone.ads.dto.customdesign.CustomerDecisionCustomDesignRequest;
import com.capstone.ads.dto.customdesign.DesignerUpdateDescriptionCustomDesignRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomDesignsService {
    CustomDesignDTO designerCreateCustomDesign(String customDesignRequestId, CustomDesignCreateRequest request);

    CustomDesignDTO customerDecisionCustomDesign(String customDesignId, CustomerDecisionCustomDesignRequest request);

    CustomDesignDTO designerUpdateDescriptionCustomDesign(String customDesignId, DesignerUpdateDescriptionCustomDesignRequest request);

    CustomDesignDTO designerUploadImage(String customDesignId, MultipartFile file);

    List<CustomDesignDTO> findCustomDesignByCustomDesignRequest(String customDesignRequestId);

    void hardDeleteCustomDesign(String customDesignId);
}
