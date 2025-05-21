package com.capstone.ads.service.impl;

import com.capstone.ads.dto.producttypesize.ProductTypeSizeDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ProductTypeSizeMapper;
import com.capstone.ads.model.ProductTypeSize;
import com.capstone.ads.repository.internal.ProductTypeRepository;
import com.capstone.ads.repository.internal.ProductTypeSizeRepository;
import com.capstone.ads.repository.internal.SizeRepository;
import com.capstone.ads.service.ProductTypeSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public ProductTypeSizeDTO create(String productTypeId, String sizeId) {
        if (!productTypeRepository.existsById(productTypeId)) throw new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND);
        if (!sizeRepository.existsById(sizeId)) throw new AppException(ErrorCode.SIZE_NOT_FOUND);
        ProductTypeSize productTypeSize = productTypeSizeMapper.toEntity(productTypeId, sizeId);
        productTypeSize = productTypeSizeRepository.save(productTypeSize);
        return productTypeSizeMapper.toDTO(productTypeSize);
    }

    @Override
    public List<ProductTypeSizeDTO> getAllByProductTypeId(String productTypeId) {
        return productTypeSizeRepository.findByProductType_Id(productTypeId).stream()
                .map(productTypeSizeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String productTypeSizeId) {
        if(!productTypeSizeRepository.existsById(productTypeSizeId)) throw new AppException(ErrorCode.PRODUCT_TYPE_SIZE_NOT_FOUND);
        productTypeSizeRepository.deleteById(productTypeSizeId);
    }
}
