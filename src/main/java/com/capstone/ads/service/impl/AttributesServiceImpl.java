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
import com.capstone.ads.service.CostTypesService;
import com.capstone.ads.service.ProductTypesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributesServiceImpl implements AttributesService {
    ProductTypesService productTypesService;
    CostTypesService costTypesService;
    AttributesMapper attributesMapper;
    AttributesRepository attributesRepository;

    @Override
    @Transactional
    public AttributesDTO createAttribute(String productTypeId, AttributesCreateRequest request) {
        ProductTypes productTypes = productTypesService.getProductTypeById(productTypeId);

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

        String oldName = attributes.getName();
        attributesMapper.updateEntityFromRequest(request, attributes);
        attributes = attributesRepository.save(attributes);

        costTypesService.updateNewAttributeValueForCoreCostType(attributes.getProductTypes().getId(), oldName, request.getName());
        return attributesMapper.toDTO(attributes);
    }

    @Override
    public AttributesDTO findAttributeById(String attributeId) {
        Attributes attributes = getAttributeById(attributeId);
        return attributesMapper.toDTO(attributes);
    }

    @Override
    public List<AttributesDTO> findAllAttributeByProductTypeId(String productTypeId) {
        return attributesRepository.findByProductTypes_Id(productTypeId).stream()
                .map(attributesMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttributesDTO> findAllAttributeByProductTypeIdAndIsAvailable(String productTypeId, boolean isAvailable) {
        return attributesRepository.findByProductTypes_IdAndIsAvailable(productTypeId, isAvailable).stream()
                .map(attributesMapper::toDTO)
                .collect(Collectors.toList());
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

    @Override
    public Attributes getAttributeById(String attributeId) {
        return attributesRepository.findById(attributeId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
    }


}
