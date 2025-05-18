package com.capstone.ads.service.impl;

import com.capstone.ads.dto.producttype.ProductTypeCreateRequest;
import com.capstone.ads.dto.producttype.ProductTypeDTO;
import com.capstone.ads.dto.producttype.ProductTypeUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ProductTypeMapper;
import com.capstone.ads.model.ProductType;
import com.capstone.ads.repository.internal.ProductTypeRepository;
import com.capstone.ads.service.ProductTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {
    private final ProductTypeRepository repository;
    private final ProductTypeMapper mapper;

    @Override
    @Transactional
    public ProductTypeDTO create(ProductTypeCreateRequest request) {
        ProductType productType = mapper.toEntity(request);
        productType = repository.save(productType);
        return mapper.toDTO(productType);
    }

    @Override
    @Transactional
    public ProductTypeDTO update(String id, ProductTypeUpdateRequest request) {
        ProductType productType = repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
        mapper.updateEntityFromRequest(request, productType);
        productType = repository.save(productType);
        return mapper.toDTO(productType);
    }

    @Override
    public ProductTypeDTO findById(String id) {
        ProductType productType = repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
        return mapper.toDTO(productType);
    }

    @Override
    public List<ProductTypeDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND);
        }
        repository.deleteById(id);
    }
}
