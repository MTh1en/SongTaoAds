package com.capstone.ads.service;

import com.capstone.ads.dto.attribute.AttributesCreateRequest;
import com.capstone.ads.dto.attribute.AttributesDTO;
import com.capstone.ads.dto.attribute.AttributesUpdateRequest;

import java.util.List;

public interface AttributesService {
    AttributesDTO create(String productTypeId, AttributesCreateRequest request);
    AttributesDTO update(String attributeId, AttributesUpdateRequest request);
    AttributesDTO findById(String id);
    List<AttributesDTO> findAllByProductTypeId(String productTypeId);
    void delete(String attributeId);
}
