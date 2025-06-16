package com.capstone.ads.service;

import com.capstone.ads.dto.attributevalue.AttributeValuesCreateRequest;
import com.capstone.ads.dto.attributevalue.AttributeValuesDTO;
import com.capstone.ads.dto.attributevalue.AttributeValuesUpdateRequest;
import org.springframework.data.domain.Page;

public interface AttributeValuesService {
    AttributeValuesDTO createAttributeValue(String attributesId, AttributeValuesCreateRequest request);

    AttributeValuesDTO updateAttributeValueInformation(String attributeValueId, AttributeValuesUpdateRequest request);

    AttributeValuesDTO findAttributeValueById(String id);

    Page<AttributeValuesDTO> findAllAttributeValueByAttributesId(String attributesId, int page, int size);

    void hardDeleteAttributeValue(String id);
}
