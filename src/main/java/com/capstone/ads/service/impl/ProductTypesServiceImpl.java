package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageKeyFormat;
import com.capstone.ads.dto.product_type.ProductTypeCreateRequest;
import com.capstone.ads.dto.product_type.ProductTypeDTO;
import com.capstone.ads.dto.product_type.ProductTypeUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ProductTypesMapper;
import com.capstone.ads.model.ProductTypes;
import com.capstone.ads.repository.internal.ProductTypesRepository;
import com.capstone.ads.service.FileDataService;
import com.capstone.ads.service.ProductTypesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductTypesServiceImpl implements ProductTypesService {
    FileDataService fileDataService;
    ProductTypesRepository productTypesRepository;
    ProductTypesMapper productTypesMapper;

    @Override
    @Transactional
    public ProductTypeDTO createProductType(ProductTypeCreateRequest request) {
        ProductTypes productTypes = productTypesMapper.toEntity(request);
        productTypes = productTypesRepository.save(productTypes);
        return productTypesMapper.toDTO(productTypes);
    }

    @Override
    @Transactional
    public ProductTypeDTO updateProductTypeInformation(String productTypeId, ProductTypeUpdateRequest request) {
        ProductTypes productTypes = getProductTypeByIdAndAvailable(productTypeId);
        productTypesMapper.updateEntityFromRequest(request, productTypes);
        productTypes = productTypesRepository.save(productTypes);
        return productTypesMapper.toDTO(productTypes);
    }

    @Override
    public ProductTypeDTO findProductTypeByProductTypeId(String productTypeId) {
        ProductTypes productTypes = getProductTypeByIdAndAvailable(productTypeId);
        return productTypesMapper.toDTO(productTypes);
    }

    @Override
    public Page<ProductTypeDTO> findAllProductType(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return productTypesRepository.findAll(pageable)
                .map(productTypesMapper::toDTO);
    }

    @Override
    @Transactional
    public void hardDeleteProductType(String id) {
        if (!productTypesRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND);
        }
        productTypesRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProductTypeDTO uploadProductTypeImage(String productTypeId, MultipartFile file) {
        ProductTypes productTypes = getProductTypeByIdAndAvailable(productTypeId);

        String productTypeKey = generateProductTypeImageKey(productTypeId);

        fileDataService.uploadSingleFile(productTypeKey, file);
        productTypes.setImage(productTypeKey);
        productTypesRepository.save(productTypes);

        return productTypesMapper.toDTO(productTypes);
    }

    @Override
    public ProductTypes getProductTypeByIdAndAvailable(String productTypeId) {
        return productTypesRepository.findByIdAndIsAvailable(productTypeId, true)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
    }

    private String generateProductTypeImageKey(String productTypeId) {
        return String.format(S3ImageKeyFormat.PRODUCT_TYPE, productTypeId);
    }
}
