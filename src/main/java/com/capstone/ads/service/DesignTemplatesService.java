package com.capstone.ads.service;

import com.capstone.ads.dto.designtemplate.DesignTemplateCreateRequest;
import com.capstone.ads.dto.designtemplate.DesignTemplateDTO;
import com.capstone.ads.dto.designtemplate.DesignTemplateUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DesignTemplatesService {
    DesignTemplateDTO createDesignTemplate(String productTypeId, DesignTemplateCreateRequest request);

    DesignTemplateDTO updateDesignTemplateInformation(String designTemplateId, DesignTemplateUpdateRequest request);

    DesignTemplateDTO uploadDesignTemplateImage(String designTemplateId, MultipartFile file);

    DesignTemplateDTO findDesignTemplateById(String designTemplateId);

    List<DesignTemplateDTO> findDesignTemplateByProductTypeId(String productTypeId);

    List<DesignTemplateDTO> findAllDesignTemplates();

    void hardDeleteDesignTemplate(String designTemplateId);
}
