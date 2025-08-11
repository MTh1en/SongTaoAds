package com.capstone.ads.service;

import com.capstone.ads.dto.attribute.AttributesCreateRequest;
import com.capstone.ads.dto.attribute.AttributesDTO;
import com.capstone.ads.dto.attribute.AttributesUpdateRequest;
import com.capstone.ads.model.Attributes;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AttributesService {
    AttributesDTO createAttribute(String productTypeId, AttributesCreateRequest request);

    AttributesDTO updateAttributeInformation(String attributeId, AttributesUpdateRequest request);

    AttributesDTO findAttributeById(String id);

    List<AttributesDTO> findAllAttributeByProductTypeId(String productTypeId);

    List<AttributesDTO> findAllAttributeByProductTypeIdAndIsAvailable(String productTypeId, boolean isAvailable);

    void hardDeleteAttribute(String attributeId);

    //INTERNAL FUNCTION
    Attributes getAttributeByIdAndIsAvailable(String attributeId);

    Attributes getAttributeById(String attributeId);
}
