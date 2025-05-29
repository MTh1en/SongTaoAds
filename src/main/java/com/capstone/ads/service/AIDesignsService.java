package com.capstone.ads.service;

import com.capstone.ads.dto.aidesign.AIDesignCreateRequest;
import com.capstone.ads.dto.aidesign.AIDesignDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AIDesignsService {
    AIDesignDTO createAIDesign(String customerDetailId, String designTemplateId, String customerNote, MultipartFile aiImage);

    List<AIDesignDTO> findAIDesignByCustomerDetailId(String customerDetailId);

    void hardDeleteAIDesign(String AIDesignId);
}
