package com.capstone.ads.mapper;

import com.capstone.ads.dto.attributevalue.AttributeValuesCreateRequest;
import com.capstone.ads.dto.attributevalue.AttributeValuesDTO;
import com.capstone.ads.dto.attributevalue.AttributeValuesUpdateRequest;
import com.capstone.ads.model.AttributeValues;
import com.capstone.ads.model.Attributes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AttributeValuesMapper {
    @Mapping(target = "attributesId", source = "attributes.id")
    AttributeValuesDTO toDTO(AttributeValues attributeValues);

    @Mapping(target = "attributes", expression = "java(mapAttributes(attributesId))")
    AttributeValues toEntity(AttributeValuesCreateRequest request, String attributesId);

    void updateEntityFromRequest(AttributeValuesUpdateRequest request, @MappingTarget AttributeValues attributeValues);

    default Attributes mapAttributes(String attributesId) {
        if (attributesId == null) return null;
        Attributes attributes = new Attributes();
        attributes.setId(attributesId);
        return attributes;
    }
}
