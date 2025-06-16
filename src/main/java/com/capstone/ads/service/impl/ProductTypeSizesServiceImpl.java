package com.capstone.ads.service.impl;

import com.capstone.ads.dto.producttypesize.ProductTypeSizeDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ProductTypeSizesMapper;
import com.capstone.ads.model.ProductTypeSizes;
import com.capstone.ads.repository.internal.ProductTypesRepository;
import com.capstone.ads.repository.internal.ProductTypeSizesRepository;
import com.capstone.ads.repository.internal.SizesRepository;
import com.capstone.ads.service.ProductTypeSizesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductTypeSizesServiceImpl implements ProductTypeSizesService {
    private final ProductTypeSizesRepository productTypeSizesRepository;
    private final ProductTypesRepository productTypesRepository;
    private final SizesRepository sizesRepository;
    private final ProductTypeSizesMapper productTypeSizesMapper;

    @Override
    @Transactional
    public ProductTypeSizeDTO createProductTypeSize(String productTypeId, String sizeId) {
        if (!productTypesRepository.existsById(productTypeId)) throw new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND);
        if (!sizesRepository.existsById(sizeId)) throw new AppException(ErrorCode.SIZE_NOT_FOUND);
        ProductTypeSizes productTypeSizes = productTypeSizesMapper.toEntity(productTypeId, sizeId);
        productTypeSizes = productTypeSizesRepository.save(productTypeSizes);
        return productTypeSizesMapper.toDTO(productTypeSizes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductTypeSizeDTO> findAllProductTypeSizeByProductTypeId(String productTypeId) {
        return productTypeSizesRepository.findByProductTypes_Id(productTypeId).stream()
                .map(productTypeSizesMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void hardDeleteProductTypeSize(String productTypeSizeId) {
        if(!productTypeSizesRepository.existsById(productTypeSizeId)) throw new AppException(ErrorCode.PRODUCT_TYPE_SIZE_NOT_FOUND);
        productTypeSizesRepository.deleteById(productTypeSizeId);
    }
}
