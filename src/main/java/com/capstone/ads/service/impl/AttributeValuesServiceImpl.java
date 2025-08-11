package com.capstone.ads.service.impl;

import com.capstone.ads.dto.attribute_value.AttributeValuesCreateRequest;
import com.capstone.ads.dto.attribute_value.AttributeValuesDTO;
import com.capstone.ads.dto.attribute_value.AttributeValuesUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.AttributeValuesMapper;
import com.capstone.ads.model.AttributeValues;
import com.capstone.ads.model.Attributes;
import com.capstone.ads.repository.internal.AttributeValuesRepository;
import com.capstone.ads.service.AttributeValuesService;
import com.capstone.ads.service.AttributesService;
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
public class AttributeValuesServiceImpl implements AttributeValuesService {
    AttributesService attributesService;
    AttributeValuesRepository attributeValuesRepository;
    AttributeValuesMapper attributeValuesMapper;

    @Override
    @Transactional
    public AttributeValuesDTO createAttributeValue(String attributesId, AttributeValuesCreateRequest request) {
        Attributes attributes = attributesService.getAttributeById(attributesId);

        AttributeValues attributeValues = attributeValuesMapper.mapCreateRequestToEntity(request);
        attributeValues.setAttributes(attributes);
        attributeValues = attributeValuesRepository.save(attributeValues);

        return attributeValuesMapper.toDTO(attributeValues);
    }

    @Override
    @Transactional
    public AttributeValuesDTO updateAttributeValueInformation(String attributeValueId, AttributeValuesUpdateRequest request) {
        AttributeValues attributeValues = attributeValuesRepository.findById(attributeValueId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND));

        attributeValuesMapper.updateEntityFromRequest(request, attributeValues);
        attributeValues = attributeValuesRepository.save(attributeValues);
        return attributeValuesMapper.toDTO(attributeValues);
    }

    @Override
    public AttributeValuesDTO findAttributeValueById(String attributeValueId) {
        AttributeValues attributeValues = attributeValuesRepository.findById(attributeValueId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND));
        return attributeValuesMapper.toDTO(attributeValues);
    }

    @Override
    public Page<AttributeValuesDTO> findAllAttributeValueByAttributesId(String attributesId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return attributeValuesRepository.findByAttributes_Id(attributesId, pageable)
                .map(attributeValuesMapper::toDTO);
    }

    @Override
    @Transactional
    public void hardDeleteAttributeValue(String attributeValueId) {
        if (!attributeValuesRepository.existsById(attributeValueId))
            throw new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND);

        attributeValuesRepository.deleteById(attributeValueId);
    }

    @Override
    public AttributeValues getAttributeValueById(String attributeValueId) {
        return attributeValuesRepository.findById(attributeValueId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND));
    }
}
