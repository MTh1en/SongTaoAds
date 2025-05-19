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
    private final ProductTypeRepository productTypeRepository;
    private final ProductTypeMapper productTypeMapper;

    @Override
    @Transactional
    public ProductTypeDTO create(ProductTypeCreateRequest request) {
        ProductType productType = productTypeMapper.toEntity(request);
        productType = productTypeRepository.save(productType);
        return productTypeMapper.toDTO(productType);
    }

    @Override
    @Transactional
    public ProductTypeDTO update(String id, ProductTypeUpdateRequest request) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
        productTypeMapper.updateEntityFromRequest(request, productType);
        productType = productTypeRepository.save(productType);
        return productTypeMapper.toDTO(productType);
    }

    @Override
    public ProductTypeDTO findById(String id) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
        return productTypeMapper.toDTO(productType);
    }

    @Override
    public List<ProductTypeDTO> findAll() {
        return productTypeRepository.findAll().stream()
                .map(productTypeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!productTypeRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND);
        }
        productTypeRepository.deleteById(id);
    }
}
