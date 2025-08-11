package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageKeyFormat;
import com.capstone.ads.dto.design_template.DesignTemplateCreateRequest;
import com.capstone.ads.dto.design_template.DesignTemplateDTO;
import com.capstone.ads.dto.design_template.DesignTemplateUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.DesignTemplatesMapper;
import com.capstone.ads.model.*;
import com.capstone.ads.model.enums.AspectRatio;
import com.capstone.ads.model.enums.DimensionType;
import com.capstone.ads.repository.internal.DesignTemplatesRepository;
import com.capstone.ads.service.*;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.capstone.ads.utils.LookupMapUtils.mapProductTypeSizesByDimensionAndSize;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DesignTemplatesServiceImpl implements DesignTemplatesService {
    ProductTypesService productTypesService;
    CustomerChoicesService customerChoicesService;
    FileDataService fileDataService;
    DesignTemplatesRepository designTemplatesRepository;
    DesignTemplatesMapper designTemplatesMapper;
    SecurityContextUtils securityContextUtils;

    @Override
    @Transactional
    public DesignTemplateDTO createDesignTemplate(String productTypeId, DesignTemplateCreateRequest request) {
        ProductTypes productTypes = productTypesService.getProductTypeById(productTypeId);
        Users currentUser = securityContextUtils.getCurrentUser();
        String imageUrl = uploadDesignTemplateImageToS3(productTypeId, request.getDesignTemplateImage());

        DesignTemplates designTemplates = designTemplatesMapper.mapCreateRequestToEntity(request);
        designTemplates.setProductTypes(productTypes);
        designTemplates.setImage(imageUrl);
        designTemplates.setUsers(currentUser);
        designTemplates.setIsAvailable(true);
        designTemplatesRepository.save(designTemplates);

        return designTemplatesMapper.toDTO(designTemplates);
    }

    @Override
    @Transactional
    public DesignTemplateDTO updateDesignTemplateInformation(String designTemplateId, DesignTemplateUpdateRequest request) {
        DesignTemplates designTemplates = getDesignTemplateByIdAndAvailable(designTemplateId);

        designTemplatesMapper.updateEntityFromRequest(request, designTemplates);
        designTemplates = designTemplatesRepository.save(designTemplates);
        return designTemplatesMapper.toDTO(designTemplates);
    }

    @Override
    @Transactional
    public DesignTemplateDTO uploadDesignTemplateImage(String designTemplateId, MultipartFile file) {
        DesignTemplates designTemplates = getDesignTemplateByIdAndAvailable(designTemplateId);
        String productTypeId = designTemplates.getProductTypes().getId();
        fileDataService.hardDeleteFileDataByImageUrl(designTemplates.getImage());

        String imageUrl = uploadDesignTemplateImageToS3(productTypeId, file);
        designTemplates.setImage(imageUrl);

        designTemplatesRepository.save(designTemplates);
        return designTemplatesMapper.toDTO(designTemplates);
    }

    @Override
    public DesignTemplateDTO findDesignTemplateById(String designTemplateId) {
        var designTemplates = getDesignTemplateById(designTemplateId);
        return designTemplatesMapper.toDTO(designTemplates);
    }

    @Override
    public Page<DesignTemplateDTO> findDesignTemplateByProductTypeId(String productTypeId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return designTemplatesRepository.findByProductTypes_IdAndIsAvailable(productTypeId, true, pageable)
                .map(designTemplatesMapper::toDTO);
    }

    @Override
    public Page<DesignTemplateDTO> findAllDesignTemplates(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return designTemplatesRepository.findByIsAvailable(true, pageable)
                .map(designTemplatesMapper::toDTO);
    }

    @Override
    @Transactional
    public void hardDeleteDesignTemplate(String designTemplateId) {
        if (!designTemplatesRepository.existsById(designTemplateId)) {
            throw new AppException(ErrorCode.DESIGN_TEMPLATE_NOT_FOUND);
        }
        designTemplatesRepository.deleteById(designTemplateId);
    }

    @Override
    public DesignTemplates getDesignTemplateById(String designTemplateId) {
        return designTemplatesRepository.findByIdAndIsAvailable(designTemplateId, true)
                .orElseThrow(() -> new AppException(ErrorCode.DESIGN_TEMPLATE_NOT_FOUND));
    }

    public Page<DesignTemplateDTO> suggestDesignTemplatesBaseCustomerChoice(String customerChoiceId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        CustomerChoices customerChoices = customerChoicesService.getCustomerChoiceById(customerChoiceId);
        Float width = 0.0F, height = 0.0F;

        Map<String, Map<DimensionType, ProductTypeSizes>> productTypeSizesLookup = mapProductTypeSizesByDimensionAndSize(customerChoices);

        List<CustomerChoiceSizes> sizes = customerChoices.getCustomerChoiceSizes();
        for (CustomerChoiceSizes customerChoiceSize : sizes) {
            Map<DimensionType, ProductTypeSizes> dimTypeMap = productTypeSizesLookup.get(customerChoiceSize.getSizes().getId());

            ProductTypeSizes widthPts = dimTypeMap.get(DimensionType.WIDTH);
            ProductTypeSizes heightPts = dimTypeMap.get(DimensionType.HEIGHT);

            if (widthPts != null) {
                width = customerChoiceSize.getSizeValue(); // Giá trị của CustomerChoiceSize
            }
            if (heightPts != null) {
                height = customerChoiceSize.getSizeValue(); // Giá trị của CustomerChoiceSize
            }
        }

        float imageRatio = width / height;

        if (imageRatio > 1) {
            return getDesignTemplateByAspecRatioAndAvailable(AspectRatio.HORIZONTAL, pageable);
        } else if (imageRatio < 1) {
            return getDesignTemplateByAspecRatioAndAvailable(AspectRatio.VERTICAL, pageable);
        } else {
            return getDesignTemplateByAspecRatioAndAvailable(AspectRatio.SQUARE, pageable);
        }
    }

    private DesignTemplates getDesignTemplateByIdAndAvailable(String designTemplateId) {
        return designTemplatesRepository.findByIdAndIsAvailable(designTemplateId, true)
                .orElseThrow(() -> new AppException(ErrorCode.DESIGN_TEMPLATE_NOT_FOUND));
    }

    private Page<DesignTemplateDTO> getDesignTemplateByAspecRatioAndAvailable(AspectRatio aspectRatio, Pageable pageable) {
        return designTemplatesRepository.findByAspectRatioAndIsAvailable(aspectRatio, true, pageable)
                .map(designTemplatesMapper::toDTO);
    }

    private String generateDesignTemplateKey(String productTypeId) {
        return String.format(S3ImageKeyFormat.DESIGN_TEMPLATE, productTypeId, UUID.randomUUID());
    }

    private String uploadDesignTemplateImageToS3(String productTypeId, MultipartFile file) {
        String designTemplateImageKey = generateDesignTemplateKey(productTypeId);
        fileDataService.uploadSingleFile(designTemplateImageKey, file);
        return designTemplateImageKey;
    }
}
