package com.capstone.ads.mapper;

import com.capstone.ads.dto.design_template.DesignTemplateCreateRequest;
import com.capstone.ads.dto.design_template.DesignTemplateDTO;
import com.capstone.ads.dto.design_template.DesignTemplateUpdateRequest;
import com.capstone.ads.model.DesignTemplates;
import com.capstone.ads.model.ProductTypes;
import com.capstone.ads.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DesignTemplatesMapper {
    DesignTemplateDTO toDTO(DesignTemplates designTemplates);

    DesignTemplates mapCreateRequestToEntity(DesignTemplateCreateRequest request);

    void updateEntityFromRequest(DesignTemplateUpdateRequest request, @MappingTarget DesignTemplates designTemplates);
}
