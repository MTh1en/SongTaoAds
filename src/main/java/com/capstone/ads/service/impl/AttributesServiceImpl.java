package com.capstone.ads.service.impl;

import com.capstone.ads.dto.attribute.AttributesCreateRequest;
import com.capstone.ads.dto.attribute.AttributesDTO;
import com.capstone.ads.dto.attribute.AttributesUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.AttributesMapper;
import com.capstone.ads.model.Attributes;
import com.capstone.ads.model.ProductTypes;
import com.capstone.ads.repository.internal.AttributesRepository;
import com.capstone.ads.service.AttributesService;
import com.capstone.ads.service.ProductTypesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributesServiceImpl implements AttributesService {
    ProductTypesService productTypesService;
    AttributesMapper attributesMapper;
    AttributesRepository attributesRepository;

    @Override
    @Transactional
    public AttributesDTO createAttribute(String productTypeId, AttributesCreateRequest request) {
        ProductTypes productTypes = productTypesService.getProductTypeByIdAndAvailable(productTypeId);

        Attributes attributes = attributesMapper.mapCreateRequestToEntity(productTypeId, request);
        attributes.setProductTypes(productTypes);
        attributes = attributesRepository.save(attributes);

        return attributesMapper.toDTO(attributes);
    }

    @Override
    @Transactional
    public AttributesDTO updateAttributeInformation(String attributeId, AttributesUpdateRequest request) {
        Attributes attributes = attributesRepository.findById(attributeId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));

        attributesMapper.updateEntityFromRequest(request, attributes);
        attributes = attributesRepository.save(attributes);
        return attributesMapper.toDTO(attributes);
    }

    @Override
    public AttributesDTO findAttributeById(String productTypeId) {
        Attributes attributes = attributesRepository.findById(productTypeId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
        return attributesMapper.toDTO(attributes);
    }

    @Override
    public Page<AttributesDTO> findAllAttributeByProductTypeId(String productTypeId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return attributesRepository.findByProductTypes_Id(productTypeId, pageable)
                .map(attributesMapper::toDTO);
    }

    @Override
    @Transactional
    public void hardDeleteAttribute(String productTypeId) {
        if (!attributesRepository.existsById(productTypeId)) {
            throw new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND);
        }
        attributesRepository.deleteById(productTypeId);
    }

    @Override
    public Attributes getAttributeByIdAndIsAvailable(String attributeId) {
        return attributesRepository.findByIdAndIsAvailable(attributeId, true)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
    }
}
