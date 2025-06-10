package com.capstone.ads.service;

import com.capstone.ads.dto.customdesign.CustomDesignDTO;
import com.capstone.ads.dto.customdesign.CustomerDecisionCustomDesignRequest;
import com.capstone.ads.dto.customdesign.DesignerUpdateDescriptionCustomDesignRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomDesignsService {
    CustomDesignDTO designerCreateCustomDesign(String customDesignRequestId, String designerDescription, MultipartFile customDesignImage);

    CustomDesignDTO customerDecisionCustomDesign(String customDesignId, CustomerDecisionCustomDesignRequest request);

    CustomDesignDTO designerUpdateDescriptionCustomDesign(String customDesignId, DesignerUpdateDescriptionCustomDesignRequest request);

    CustomDesignDTO designerUploadImage(String customDesignId, MultipartFile file);

    Page<CustomDesignDTO> findCustomDesignByCustomDesignRequest(String customDesignRequestId, int page, int size);

    void hardDeleteCustomDesign(String customDesignId);
}
