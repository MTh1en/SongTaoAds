package com.capstone.ads.mapper;

import com.capstone.ads.dto.attribute.AttributesCreateRequest;
import com.capstone.ads.dto.attribute.AttributesDTO;
import com.capstone.ads.dto.attribute.AttributesUpdateRequest;
import com.capstone.ads.model.Attributes;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AttributesMapper {
    AttributesDTO toDTO(Attributes attributes);

    Attributes mapCreateRequestToEntity(String productId, AttributesCreateRequest request);

    void updateEntityFromRequest(AttributesUpdateRequest request, @MappingTarget Attributes attributes);
}
