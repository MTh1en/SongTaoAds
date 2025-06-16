package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageDuration;
import com.capstone.ads.dto.producttype.ProductTypeCreateRequest;
import com.capstone.ads.dto.producttype.ProductTypeDTO;
import com.capstone.ads.dto.producttype.ProductTypeUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ProductTypesMapper;
import com.capstone.ads.model.ProductTypes;
import com.capstone.ads.repository.external.S3Repository;
import com.capstone.ads.repository.internal.ProductTypesRepository;
import com.capstone.ads.service.ProductTypesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductTypesServiceImpl implements ProductTypesService {
    @Value("${aws.bucket.name}")
    private String bucketName;

    private final ProductTypesRepository productTypesRepository;
    private final ProductTypesMapper productTypesMapper;
    private final S3Repository s3Repository;

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
        return convertProductTypeToProductTypeDTOWithImageUrl(productTypes);
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

        s3Repository.uploadSingleFile(bucketName, file, productTypeKey);
        productTypes.setImage(productTypeKey);
        productTypesRepository.save(productTypes);

        return productTypesMapper.toDTO(productTypes);
    }

    private String generateProductTypeImageKey(String productTypeId) {
        return String.format("product-type/%s/%s", productTypeId, UUID.randomUUID());
    }

    private ProductTypes getProductTypeByIdAndAvailable(String productTypeId) {
        return productTypesRepository.findByIdAndIsAvailable(productTypeId, true)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
    }

    private ProductTypeDTO convertProductTypeToProductTypeDTOWithImageUrl(ProductTypes productTypes) {
        var productTypeDTOResponse = productTypesMapper.toDTO(productTypes);

        var productTypeImage = s3Repository.generatePresignedUrl(bucketName, productTypes.getImage(), S3ImageDuration.PRODUCT_TYPE_IMAGE_DURATION);
        productTypeDTOResponse.setImage(productTypeImage);

        return productTypeDTOResponse;
    }
}
