package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageDuration;
import com.capstone.ads.dto.producttype.ProductTypeCreateRequest;
import com.capstone.ads.dto.producttype.ProductTypeDTO;
import com.capstone.ads.dto.producttype.ProductTypeUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ProductTypeMapper;
import com.capstone.ads.model.ProductType;
import com.capstone.ads.repository.external.S3Repository;
import com.capstone.ads.repository.internal.ProductTypeRepository;
import com.capstone.ads.service.ProductTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {
    @Value("${aws.bucket.name}")
    private String bucketName;

    private final ProductTypeRepository productTypeRepository;
    private final ProductTypeMapper productTypeMapper;
    private final S3Repository s3Repository;

    @Override
    @Transactional
    public ProductTypeDTO create(ProductTypeCreateRequest request) {
        ProductType productType = productTypeMapper.toEntity(request);
        productType = productTypeRepository.save(productType);
        return productTypeMapper.toDTO(productType);
    }

    @Override
    @Transactional
    public ProductTypeDTO update(String productTypeId, ProductTypeUpdateRequest request) {
        ProductType productType = getProductTypeByIdAndAvailable(productTypeId);
        productTypeMapper.updateEntityFromRequest(request, productType);
        productType = productTypeRepository.save(productType);
        return productTypeMapper.toDTO(productType);
    }

    @Override
    public ProductTypeDTO findById(String productTypeId) {
        ProductType productType = getProductTypeByIdAndAvailable(productTypeId);
        return convertProductTypeToProductTypeDTOWithImageUrl(productType);
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

    @Override
    public ProductTypeDTO uploadProductTypeImage(String productTypeId, MultipartFile file) {
        ProductType productType = getProductTypeByIdAndAvailable(productTypeId);

        String productTypeKey = generateProductTypeImageKey(productTypeId);
        byte[] fileBytes = convertMultipartFileToByteArray(file);

        s3Repository.uploadSingleFile(bucketName, fileBytes, productTypeKey, file.getContentType());
        productType.setImage(productTypeKey);
        productTypeRepository.save(productType);

        return productTypeMapper.toDTO(productType);
    }

    private String generateProductTypeImageKey(String productTypeId) {
        return String.format("product-type/%s/%s", productTypeId, UUID.randomUUID());
    }

    private ProductType getProductTypeByIdAndAvailable(String productTypeId) {
        return productTypeRepository.findByIdAndIsAvailable(productTypeId, true)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
    }

    private ProductTypeDTO convertProductTypeToProductTypeDTOWithImageUrl(ProductType productType) {
        var productTypeDTOResponse = productTypeMapper.toDTO(productType);

        var productTypeImage = s3Repository.generatePresignedUrl(bucketName, productType.getImage(), S3ImageDuration.PRODUCT_TYPE_IMAGE_DURATION);
        productTypeDTOResponse.setImage(productTypeImage);

        return productTypeDTOResponse;
    }

    private byte[] convertMultipartFileToByteArray(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException ex) {
            throw new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND);
        }
    }   
}
