package com.capstone.ads.service.impl;

import com.capstone.ads.dto.product_type_size.ProductTypeSizeDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ProductTypeSizesMapper;
import com.capstone.ads.model.ProductTypeSizes;
import com.capstone.ads.model.ProductTypes;
import com.capstone.ads.model.Sizes;
import com.capstone.ads.repository.internal.ProductTypeSizesRepository;
import com.capstone.ads.service.ProductTypeSizesService;
import com.capstone.ads.service.ProductTypesService;
import com.capstone.ads.service.SizeService;
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
public class ProductTypeSizesServiceImpl implements ProductTypeSizesService {
    ProductTypesService productTypesService;
    SizeService sizeService;
    ProductTypeSizesRepository productTypeSizesRepository;
    ProductTypeSizesMapper productTypeSizesMapper;

    @Override
    @Transactional
    public ProductTypeSizeDTO createProductTypeSize(String productTypeId, String sizeId) {
        ProductTypes productTypes = productTypesService.getProductTypeByIdAndAvailable(productTypeId);
        Sizes sizes = sizeService.getSizeByIdAndIsAvailable(sizeId);

        ProductTypeSizes productTypeSizes = ProductTypeSizes.builder()
                .productTypes(productTypes)
                .sizes(sizes)
                .build();
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
        if (!productTypeSizesRepository.existsById(productTypeSizeId))
            throw new AppException(ErrorCode.PRODUCT_TYPE_SIZE_NOT_FOUND);
        productTypeSizesRepository.deleteById(productTypeSizeId);
    }

    @Override
    public void validateProductTypeSizeExist(String productTypeId, String sizeId) {
        if (!productTypeSizesRepository.existsByProductTypes_IdAndSizes_Id(productTypeId, sizeId)) {
            throw new AppException(ErrorCode.SIZE_NOT_BELONG_PRODUCT_TYPE);
        }
    }

    @Override
    public void validateProductTypeSizeMaxValueAndMinValue(String productTypeId, String sizeId, Float sizeValue) {
        var productTypeSize = productTypeSizesRepository.findByProductTypes_IdAndSizes_Id(productTypeId, sizeId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_SIZE_NOT_FOUND));
        Float maxValue = productTypeSize.getMaxValue();
        Float minValue = productTypeSize.getMinValue();

        if (sizeValue > maxValue && sizeValue < minValue) {
            throw new AppException(ErrorCode.SIZE_VALUE_OUT_OF_RANGE);
        }
    }
}
