package com.capstone.ads.service.impl;

import com.capstone.ads.dto.attribute_value.AttributeValuesCreateRequest;
import com.capstone.ads.dto.attribute_value.AttributeValuesDTO;
import com.capstone.ads.dto.attribute_value.AttributeValuesUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.AttributeValuesMapper;
import com.capstone.ads.model.AttributeValues;
import com.capstone.ads.repository.internal.AttributeValuesRepository;
import com.capstone.ads.service.AttributeValuesService;
import com.capstone.ads.service.AttributesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttributeValuesServiceImpl implements AttributeValuesService {
    private final AttributesService attributesService;
    private final AttributeValuesRepository attributeValuesRepository;
    private final AttributeValuesMapper attributeValuesMapper;

    @Override
    @Transactional
    public AttributeValuesDTO createAttributeValue(String attributesId, AttributeValuesCreateRequest request) {
        attributesService.validateAttributeExistsAndIsAvailable(attributesId);

        AttributeValues attributeValues = attributeValuesMapper.toEntity(request, attributesId);
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
    public AttributeValuesDTO findAttributeValueById(String id) {
        AttributeValues attributeValues = attributeValuesRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND));
        return attributeValuesMapper.toDTO(attributeValues);
    }

    @Override
    public Page<AttributeValuesDTO> findAllAttributeValueByAttributesId(String attributesId, int page, int size) {
        attributesService.validateAttributeExistsAndIsAvailable(attributesId);

        Pageable pageable = PageRequest.of(page - 1, size);

        return attributeValuesRepository.findByAttributes_Id(attributesId, pageable)
                .map(attributeValuesMapper::toDTO);
    }

    @Override
    @Transactional
    public void hardDeleteAttributeValue(String id) {
        if (!attributeValuesRepository.existsById(id))
            throw new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND);

        attributeValuesRepository.deleteById(id);
    }
}
