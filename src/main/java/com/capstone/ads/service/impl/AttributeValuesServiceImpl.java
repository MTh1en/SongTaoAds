package com.capstone.ads.service.impl;

import com.capstone.ads.dto.attributevalue.AttributeValuesCreateRequest;
import com.capstone.ads.dto.attributevalue.AttributeValuesDTO;
import com.capstone.ads.dto.attributevalue.AttributeValuesUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.AttributeValuesMapper;
import com.capstone.ads.model.AttributeValues;
import com.capstone.ads.repository.internal.AttributeValuesRepository;
import com.capstone.ads.repository.internal.AttributesRepository;
import com.capstone.ads.service.AttributeValuesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttributeValuesServiceImpl implements AttributeValuesService {
    private final AttributeValuesRepository attributeValuesRepository;
    private final AttributesRepository attributesRepository;
    private final AttributeValuesMapper attributeValuesMapper;

    @Override
    @Transactional
    public AttributeValuesDTO create(String attributesId, AttributeValuesCreateRequest request) {
        if (!attributesRepository.existsById(attributesId))
            throw new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND);

        AttributeValues attributeValues = attributeValuesMapper.toEntity(request, attributesId);
        attributeValues = attributeValuesRepository.save(attributeValues);
        return attributeValuesMapper.toDTO(attributeValues);
    }

    @Override
    @Transactional
    public AttributeValuesDTO update(String attributeValueId, AttributeValuesUpdateRequest request) {
        AttributeValues attributeValues = attributeValuesRepository.findById(attributeValueId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND));

        attributeValuesMapper.updateEntityFromRequest(request, attributeValues);
        attributeValues = attributeValuesRepository.save(attributeValues);
        return attributeValuesMapper.toDTO(attributeValues);
    }

    @Override
    public AttributeValuesDTO findById(String id) {
        AttributeValues attributeValues = attributeValuesRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND));
        return attributeValuesMapper.toDTO(attributeValues);
    }

    @Override
    public Page<AttributeValuesDTO> findAllByAttributesId(String attributesId, int page, int size) {
        if (!attributesRepository.existsById(attributesId))
            throw new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND);

        Pageable pageable = PageRequest.of(page - 1, size);

        return attributeValuesRepository.findByAttributes_Id(attributesId, pageable)
                .map(attributeValuesMapper::toDTO);
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!attributeValuesRepository.existsById(id))
            throw new AppException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND);

        attributeValuesRepository.deleteById(id);
    }
}
