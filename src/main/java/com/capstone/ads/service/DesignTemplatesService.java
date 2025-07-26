package com.capstone.ads.service;

import com.capstone.ads.dto.design_template.DesignTemplateCreateRequest;
import com.capstone.ads.dto.design_template.DesignTemplateDTO;
import com.capstone.ads.dto.design_template.DesignTemplateUpdateRequest;
import com.capstone.ads.model.DesignTemplates;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface DesignTemplatesService {
    DesignTemplateDTO createDesignTemplate(String productTypeId, DesignTemplateCreateRequest request);

    DesignTemplateDTO updateDesignTemplateInformation(String designTemplateId, DesignTemplateUpdateRequest request);

    DesignTemplateDTO uploadDesignTemplateImage(String designTemplateId, MultipartFile file);

    DesignTemplateDTO findDesignTemplateById(String designTemplateId);

    Page<DesignTemplateDTO> findDesignTemplateByProductTypeId(String productTypeId, int page, int size);

    Page<DesignTemplateDTO> findAllDesignTemplates(int page, int size);

    Page<DesignTemplateDTO> suggestDesignTemplatesBaseCustomerChoice(String customerChoiceId, int page, int size);

    void hardDeleteDesignTemplate(String designTemplateId);

    //INTERNAL FUNCTION
    DesignTemplates getDesignTemplateById(String designTemplateId);
}
