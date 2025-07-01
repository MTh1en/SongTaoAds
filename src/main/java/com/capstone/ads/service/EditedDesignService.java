package com.capstone.ads.service;

import com.capstone.ads.dto.edited_design.EditedDesignDTO;
import com.capstone.ads.model.EditedDesigns;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface EditedDesignService {
    EditedDesignDTO createEditedDesign(String customerDetailId, String designTemplateId, String customerNote, MultipartFile aiImage);

    Page<EditedDesignDTO> findEditedDesignByCustomerDetailId(String customerDetailId, int page, int size);

    void hardDeleteEditedDesign(String AIDesignId);

    //INTERNAL FUNCTION
    EditedDesigns getEditedDesignById(String AIDesignId);
}
