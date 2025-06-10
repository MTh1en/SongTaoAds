package com.capstone.ads.service;

import com.capstone.ads.dto.aidesign.AIDesignDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface AIDesignsService {
    AIDesignDTO createAIDesign(String customerDetailId, String designTemplateId, String customerNote, MultipartFile aiImage);

    Page<AIDesignDTO> findAIDesignByCustomerDetailId(String customerDetailId, int page, int size);

    void hardDeleteAIDesign(String AIDesignId);
}
