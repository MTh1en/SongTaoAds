package com.capstone.ads.mapper;

import com.capstone.ads.dto.attribute_value.AttributeValuesCreateRequest;
import com.capstone.ads.dto.attribute_value.AttributeValuesDTO;
import com.capstone.ads.dto.attribute_value.AttributeValuesUpdateRequest;
import com.capstone.ads.model.AttributeValues;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AttributeValuesMapper {
    AttributeValuesDTO toDTO(AttributeValues attributeValues);

    AttributeValues mapCreateRequestToEntity(AttributeValuesCreateRequest request);

    void updateEntityFromRequest(AttributeValuesUpdateRequest request, @MappingTarget AttributeValues attributeValues);
}
