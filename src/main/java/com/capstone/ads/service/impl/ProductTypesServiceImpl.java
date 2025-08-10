package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageKeyFormat;
import com.capstone.ads.dto.product_type.ProductTypeCreateRequest;
import com.capstone.ads.dto.product_type.ProductTypeDTO;
import com.capstone.ads.dto.product_type.ProductTypeUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ProductTypesMapper;
import com.capstone.ads.model.CostTypes;
import com.capstone.ads.model.ProductTypes;
import com.capstone.ads.repository.internal.ProductTypesRepository;
import com.capstone.ads.service.FileDataService;
import com.capstone.ads.service.ProductTypesService;
import com.capstone.ads.utils.DataConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductTypesServiceImpl implements ProductTypesService {
    FileDataService fileDataService;
    ProductTypesRepository productTypesRepository;
    ProductTypesMapper productTypesMapper;

    @Override
    @Transactional
    public ProductTypeDTO createProductType(ProductTypeCreateRequest request) {
        ProductTypes productTypes = productTypesMapper.toEntity(request);

        if (!Objects.isNull(request.getProductTypeImage())) {
            String productTypeKey = generateProductTypeImageKey();
            fileDataService.uploadSingleFile(productTypeKey, request.getProductTypeImage());
            productTypes.setImage(productTypeKey);
        }

        productTypes = productTypesRepository.save(productTypes);
        return productTypesMapper.toDTO(productTypes);
    }

    @Override
    @Transactional
    public ProductTypeDTO uploadProductTypeImage(String productTypeId, MultipartFile file) {
        ProductTypes productTypes = getProductTypeByIdAndAvailable(productTypeId);

        String productTypeKey = generateProductTypeImageKey();

        fileDataService.uploadSingleFile(productTypeKey, file);
        productTypes.setImage(productTypeKey);
        productTypesRepository.save(productTypes);

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

    //INTERNAL FUNCTION//
    @Override
    public ProductTypes getProductTypeByIdAndAvailable(String productTypeId) {
        return productTypesRepository.findByIdAndIsAvailable(productTypeId, true)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
    }

    @Override
    public ProductTypes getProductTypeById(String productTypeId) {
        return productTypesRepository.findById(productTypeId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
    }

    @Async
    @Override
    @Transactional
    public void addCostTypeToCalculateFormula(String productTypeId, String costTypeName) {
        ProductTypes productTypes = productTypesRepository.findById(productTypeId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));

        productTypes.setCalculateFormula(
                DataConverter.appendToFormula(productTypes.getCalculateFormula(), costTypeName)
        );
        productTypes.setIsAvailable(true);
        productTypesRepository.save(productTypes);
    }

    @Async
    @Override
    @Transactional
    public void updateNewValueNameCalculateFormula(String productTypeId, String oldName, String newName) {
        ProductTypes productTypes = productTypesRepository.findById(productTypeId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
        productTypes.setCalculateFormula(DataConverter.replaceFormulaValue(
                productTypes.getCalculateFormula(), oldName, newName
        ));
        productTypesRepository.save(productTypes);
    }

    @Async
    @Override
    @Transactional
    public void updateAllFormulaCostTypeValues(String productTypeId, String oldName, String newName) {
        ProductTypes productTypes = getProductTypeById(productTypeId);
        List<CostTypes> costTypes = productTypes.getCostTypes();

        for (CostTypes costType : costTypes) {
            String newFormula = DataConverter.replaceFormulaValue(costType.getFormula(), oldName, newName);
            costType.setFormula(newFormula);
        }

        productTypes.setCostTypes(costTypes);
        productTypesRepository.save(productTypes);
    }

    private String generateProductTypeImageKey() {
        return String.format(S3ImageKeyFormat.PRODUCT_TYPE, UUID.randomUUID());
    }
}
