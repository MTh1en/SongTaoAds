package com.capstone.ads.service.impl;

import com.capstone.ads.dto.attribute.AttributesCreateRequest;
import com.capstone.ads.dto.attribute.AttributesDTO;
import com.capstone.ads.dto.attribute.AttributesUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.AttributesMapper;
import com.capstone.ads.model.Attributes;
import com.capstone.ads.repository.internal.AttributesRepository;
import com.capstone.ads.repository.internal.ProductTypeRepository;
import com.capstone.ads.service.AttributesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttributesServiceImpl implements AttributesService {
    private final AttributesRepository attributesRepository;
    private final ProductTypeRepository productTypeRepository;
    private final AttributesMapper attributesMapper;

    @Override
    @Transactional
    public AttributesDTO create(String productTypeId, AttributesCreateRequest request) {
        productTypeRepository.findById(productTypeId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));

        Attributes attributes = attributesMapper.toEntity(productTypeId, request);
        attributes = attributesRepository.save(attributes);
        return attributesMapper.toDTO(attributes);
    }

    @Override
    @Transactional
    public AttributesDTO update(String attributeId, AttributesUpdateRequest request) {
        Attributes attributes = attributesRepository.findById(attributeId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));

        attributesMapper.updateEntityFromRequest(request, attributes);
        attributes = attributesRepository.save(attributes);
        return attributesMapper.toDTO(attributes);
    }

    @Override
    public AttributesDTO findById(String productTypeId) {
        Attributes attributes = attributesRepository.findById(productTypeId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
        return attributesMapper.toDTO(attributes);
    }

    @Override
    public List<AttributesDTO> findAllByProductTypeId(String productTypeId) {
        // Validate productTypeId
        productTypeRepository.findById(productTypeId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));

        return attributesRepository.findByProductTypes_Id(productTypeId).stream()
                .map(attributesMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String productTypeId) {
        if (!attributesRepository.existsById(productTypeId)) {
            throw new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND);
        }
        attributesRepository.deleteById(productTypeId);
    }
}
