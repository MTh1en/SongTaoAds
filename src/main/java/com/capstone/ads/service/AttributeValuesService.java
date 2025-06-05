package com.capstone.ads.service;

import com.capstone.ads.dto.attributevalue.AttributeValuesCreateRequest;
import com.capstone.ads.dto.attributevalue.AttributeValuesDTO;
import com.capstone.ads.dto.attributevalue.AttributeValuesUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AttributeValuesService {
    AttributeValuesDTO create(String attributesId, AttributeValuesCreateRequest request);

    AttributeValuesDTO update(String attributeValueId, AttributeValuesUpdateRequest request);

    AttributeValuesDTO findById(String id);

    Page<AttributeValuesDTO> findAllByAttributesId(String attributesId, int page, int size);

    void delete(String id);
}
