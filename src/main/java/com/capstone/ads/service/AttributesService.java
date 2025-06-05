package com.capstone.ads.service;

import com.capstone.ads.dto.attribute.AttributesCreateRequest;
import com.capstone.ads.dto.attribute.AttributesDTO;
import com.capstone.ads.dto.attribute.AttributesUpdateRequest;
import org.springframework.data.domain.Page;

public interface AttributesService {
    AttributesDTO create(String productTypeId, AttributesCreateRequest request);

    AttributesDTO update(String attributeId, AttributesUpdateRequest request);

    AttributesDTO findById(String id);

    Page<AttributesDTO> findAllByProductTypeId(String productTypeId, int page, int size);

    void delete(String attributeId);
}
