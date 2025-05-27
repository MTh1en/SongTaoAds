package com.capstone.ads.service.impl;

import com.capstone.ads.dto.producttypesize.ProductTypeSizeDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ProductTypeSizeMapper;
import com.capstone.ads.model.ProductTypeSizes;
import com.capstone.ads.repository.internal.ProductTypeRepository;
import com.capstone.ads.repository.internal.ProductTypeSizeRepository;
import com.capstone.ads.repository.internal.SizeRepository;
import com.capstone.ads.service.ProductTypeSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductTypeSizeServiceImpl implements ProductTypeSizeService {
    private final ProductTypeSizeRepository productTypeSizeRepository;
    private final ProductTypeRepository productTypeRepository;
    private final SizeRepository sizeRepository;
    private final ProductTypeSizeMapper productTypeSizeMapper;

    @Override
    @Transactional
    public ProductTypeSizeDTO create(String productTypeId, String sizeId) {
        if (!productTypeRepository.existsById(productTypeId)) throw new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND);
        if (!sizeRepository.existsById(sizeId)) throw new AppException(ErrorCode.SIZE_NOT_FOUND);
        ProductTypeSizes productTypeSizes = productTypeSizeMapper.toEntity(productTypeId, sizeId);
        productTypeSizes = productTypeSizeRepository.save(productTypeSizes);
        return productTypeSizeMapper.toDTO(productTypeSizes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductTypeSizeDTO> getAllByProductTypeId(String productTypeId) {
        return productTypeSizeRepository.findByProductTypes_Id(productTypeId).stream()
                .map(productTypeSizeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String productTypeSizeId) {
        if(!productTypeSizeRepository.existsById(productTypeSizeId)) throw new AppException(ErrorCode.PRODUCT_TYPE_SIZE_NOT_FOUND);
        productTypeSizeRepository.deleteById(productTypeSizeId);
    }
}
