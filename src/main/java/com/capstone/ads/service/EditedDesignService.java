package com.capstone.ads.service;

import com.capstone.ads.dto.edited_design.EditedDesignCreateRequest;
import com.capstone.ads.dto.edited_design.EditedDesignDTO;
import com.capstone.ads.model.EditedDesigns;
import org.springframework.data.domain.Page;

public interface EditedDesignService {
    EditedDesignDTO createEditedDesignFromDesignTemplate(String customerDetailId, String designTemplateId,
                                                         EditedDesignCreateRequest request);

    EditedDesignDTO createEditedDesignFromBackground(String customerDetailId, String backgroundId,
                                                     EditedDesignCreateRequest request);

    Page<EditedDesignDTO> findEditedDesignByCustomerDetailId(String customerDetailId, int page, int size);

    EditedDesignDTO findEditedDesignById(String editedDesignId);

    void hardDeleteEditedDesign(String AIDesignId);

    //INTERNAL FUNCTION
    EditedDesigns getEditedDesignById(String AIDesignId);
}
