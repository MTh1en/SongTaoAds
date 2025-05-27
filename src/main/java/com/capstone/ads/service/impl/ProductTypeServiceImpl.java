package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageDuration;
import com.capstone.ads.dto.producttype.ProductTypeCreateRequest;
import com.capstone.ads.dto.producttype.ProductTypeDTO;
import com.capstone.ads.dto.producttype.ProductTypeUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ProductTypeMapper;
import com.capstone.ads.model.ProductTypes;
import com.capstone.ads.repository.external.S3Repository;
import com.capstone.ads.repository.internal.ProductTypeRepository;
import com.capstone.ads.service.ProductTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    public ProductTypeDTO createProductTypeService(ProductTypeCreateRequest request) {
        ProductTypes productTypes = productTypeMapper.toEntity(request);
        productTypes = productTypeRepository.save(productTypes);
        return productTypeMapper.toDTO(productTypes);
    }

    @Override
    @Transactional
    public ProductTypeDTO updateInformation(String productTypeId, ProductTypeUpdateRequest request) {
        ProductTypes productTypes = getProductTypeByIdAndAvailable(productTypeId);
        productTypeMapper.updateEntityFromRequest(request, productTypes);
        productTypes = productTypeRepository.save(productTypes);
        return productTypeMapper.toDTO(productTypes);
    }

    @Override
    public ProductTypeDTO findProductTypeByProductTypeId(String productTypeId) {
        ProductTypes productTypes = getProductTypeByIdAndAvailable(productTypeId);
        return convertProductTypeToProductTypeDTOWithImageUrl(productTypes);
    }

    @Override
    public List<ProductTypeDTO> findAllProductType() {
        return productTypeRepository.findAll().stream()
                .map(productTypeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void forceDeleteProductType(String id) {
        if (!productTypeRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND);
        }
        productTypeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProductTypeDTO uploadProductTypeImage(String productTypeId, MultipartFile file) {
        ProductTypes productTypes = getProductTypeByIdAndAvailable(productTypeId);

        String productTypeKey = generateProductTypeImageKey(productTypeId);

        s3Repository.uploadSingleFile(bucketName, file, productTypeKey);
        productTypes.setImage(productTypeKey);
        productTypeRepository.save(productTypes);

        return productTypeMapper.toDTO(productTypes);
    }

    private String generateProductTypeImageKey(String productTypeId) {
        return String.format("product-type/%s/%s", productTypeId, UUID.randomUUID());
    }

    private ProductTypes getProductTypeByIdAndAvailable(String productTypeId) {
        return productTypeRepository.findByIdAndIsAvailable(productTypeId, true)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
    }

    private ProductTypeDTO convertProductTypeToProductTypeDTOWithImageUrl(ProductTypes productTypes) {
        var productTypeDTOResponse = productTypeMapper.toDTO(productTypes);

        var productTypeImage = s3Repository.generatePresignedUrl(bucketName, productTypes.getImage(), S3ImageDuration.PRODUCT_TYPE_IMAGE_DURATION);
        productTypeDTOResponse.setImage(productTypeImage);

        return productTypeDTOResponse;
    }
}
