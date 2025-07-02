package com.capstone.ads.mapper;

import com.capstone.ads.dto.edited_design.EditedDesignCreateRequest;
import com.capstone.ads.dto.edited_design.EditedDesignDTO;
import com.capstone.ads.model.EditedDesigns;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EditedDesignMapper {
    EditedDesignDTO toDTO(EditedDesigns editedDesigns);

    @Mapping(target = "editedImage", ignore = true)
    EditedDesigns mapCreateRequestToEntity(EditedDesignCreateRequest request);
}
