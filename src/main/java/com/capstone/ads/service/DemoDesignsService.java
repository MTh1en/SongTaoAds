package com.capstone.ads.service;

import com.capstone.ads.dto.demo_design.DemoDesignCreateRequest;
import com.capstone.ads.dto.demo_design.DemoDesignDTO;
import com.capstone.ads.dto.demo_design.CustomerRejectCustomDesignRequest;
import com.capstone.ads.dto.demo_design.DesignerUpdateDescriptionCustomDesignRequest;
import com.capstone.ads.dto.file.FileDataDTO;
import com.capstone.ads.dto.file.UploadMultipleFileRequest;
import com.capstone.ads.model.DemoDesigns;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DemoDesignsService {
    DemoDesignDTO designerCreateCustomDesign(String customDesignRequestId, DemoDesignCreateRequest request);

    DemoDesignDTO customerApproveCustomDesign(String customDesignId);

    DemoDesignDTO customerRejectCustomDesign(String customDesignId, CustomerRejectCustomDesignRequest request);

    DemoDesignDTO designerUpdateDescriptionCustomDesign(String customDesignId, DesignerUpdateDescriptionCustomDesignRequest request);

    DemoDesignDTO designerUploadImage(String customDesignId, MultipartFile file);

    List<FileDataDTO> uploadDemoDesignSubImages(String demoDesignId, UploadMultipleFileRequest request);

    Page<DemoDesignDTO> findCustomDesignByCustomDesignRequest(String customDesignRequestId, int page, int size);

    void hardDeleteCustomDesign(String customDesignId);

    //INTERNAL FUNCTION//
    DemoDesigns getDemoDesignsById(String customDesignId);
}
