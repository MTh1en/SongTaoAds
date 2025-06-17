package com.capstone.ads.service;

import com.capstone.ads.dto.designtemplate.DesignTemplateCreateRequest;
import com.capstone.ads.dto.designtemplate.DesignTemplateDTO;
import com.capstone.ads.dto.designtemplate.DesignTemplateUpdateRequest;
import com.capstone.ads.model.DesignTemplates;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DesignTemplatesService {
    DesignTemplateDTO createDesignTemplate(String productTypeId, DesignTemplateCreateRequest request);

    DesignTemplateDTO updateDesignTemplateInformation(String designTemplateId, DesignTemplateUpdateRequest request);

    DesignTemplateDTO uploadDesignTemplateImage(String designTemplateId, MultipartFile file);

    DesignTemplateDTO findDesignTemplateById(String designTemplateId);

    Page<DesignTemplateDTO> findDesignTemplateByProductTypeId(String productTypeId, int page, int size);

    Page<DesignTemplateDTO> findAllDesignTemplates(int page, int size);

    void hardDeleteDesignTemplate(String designTemplateId);

    //INTERNAL FUNCTION
    void validateDesignTemplateExistsAndAvailable(String designTemplateId);

    DesignTemplates getDesignTemplateById(String designTemplateId);
}
