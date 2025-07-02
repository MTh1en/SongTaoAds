package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageDuration;
import com.capstone.ads.dto.design_template.DesignTemplateCreateRequest;
import com.capstone.ads.dto.design_template.DesignTemplateDTO;
import com.capstone.ads.dto.design_template.DesignTemplateUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.DesignTemplatesMapper;
import com.capstone.ads.model.DesignTemplates;
import com.capstone.ads.model.ProductTypes;
import com.capstone.ads.model.Users;
import com.capstone.ads.repository.internal.DesignTemplatesRepository;
import com.capstone.ads.service.DesignTemplatesService;
import com.capstone.ads.service.ProductTypesService;
import com.capstone.ads.service.S3Service;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class DesignTemplatesServiceImpl implements DesignTemplatesService {
    private final ProductTypesService productTypesService;
    private final S3Service s3Service;
    private final DesignTemplatesRepository designTemplatesRepository;
    private final DesignTemplatesMapper designTemplatesMapper;
    private final SecurityContextUtils securityContextUtils;

    @Override
    @Transactional
    public DesignTemplateDTO createDesignTemplate(String productTypeId, DesignTemplateCreateRequest request) {
        ProductTypes productTypes = productTypesService.getProductTypeByIdAndAvailable(productTypeId);
        Users currentUser = securityContextUtils.getCurrentUser();

        DesignTemplates designTemplates = designTemplatesMapper.mapCreateRequestToEntity(request);
        designTemplates.setProductTypes(productTypes);
        designTemplates.setUsers(currentUser);
        designTemplates.setIsAvailable(true);
        designTemplatesRepository.save(designTemplates);

        return designTemplatesMapper.toDTO(designTemplates);
    }

    @Override
    @Transactional
    public DesignTemplateDTO updateDesignTemplateInformation(String designTemplateId, DesignTemplateUpdateRequest request) {
        DesignTemplates designTemplates = findDesignTemplateByIdAndAvailable(designTemplateId);

        designTemplatesMapper.updateEntityFromRequest(request, designTemplates);
        designTemplates = designTemplatesRepository.save(designTemplates);
        return designTemplatesMapper.toDTO(designTemplates);
    }

    @Override
    @Transactional
    public DesignTemplateDTO uploadDesignTemplateImage(String designTemplateId, MultipartFile file) {
        DesignTemplates designTemplates = findDesignTemplateByIdAndAvailable(designTemplateId);
        String designTemplateImageKey = generateDesignTemplateKey(designTemplates.getProductTypes().getId(), designTemplateId);

        s3Service.uploadSingleFile(designTemplateImageKey, file);
        designTemplates.setImage(designTemplateImageKey);
        designTemplatesRepository.save(designTemplates);
        return designTemplatesMapper.toDTO(designTemplates);
    }

    @Override
    public DesignTemplateDTO findDesignTemplateById(String designTemplateId) {
        var designTemplates = findById(designTemplateId);
        return convertToDesignTemplateDTOWithImageIsPresignedURL(designTemplates);
    }

    @Override
    public Page<DesignTemplateDTO> findDesignTemplateByProductTypeId(String productTypeId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return designTemplatesRepository.findByProductTypes_IdAndIsAvailable(productTypeId, true, pageable)
                .map(this::convertToDesignTemplateDTOWithImageIsPresignedURL);
    }

    @Override
    public Page<DesignTemplateDTO> findAllDesignTemplates(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return designTemplatesRepository.findAll(pageable)
                .map(this::convertToDesignTemplateDTOWithImageIsPresignedURL);
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

    private DesignTemplates findById(String designTemplateId) {
        return designTemplatesRepository.findById(designTemplateId)
                .orElseThrow(() -> new AppException(ErrorCode.DESIGN_TEMPLATE_NOT_FOUND));
    }

    private DesignTemplates findDesignTemplateByIdAndAvailable(String designTemplateId) {
        return designTemplatesRepository.findByIdAndIsAvailable(designTemplateId, true)
                .orElseThrow(() -> new AppException(ErrorCode.DESIGN_TEMPLATE_NOT_FOUND));
    }

    private String generateDesignTemplateKey(String productTypeId, String designTemplateId) {
        return String.format("design-template/%s/%s", productTypeId, designTemplateId);
    }

    private DesignTemplateDTO convertToDesignTemplateDTOWithImageIsPresignedURL(DesignTemplates designTemplates) {
        var designTemplateDTOResponse = designTemplatesMapper.toDTO(designTemplates);
        if (!Objects.isNull(designTemplates.getImage())) {
            String designTemplateImageKey = designTemplates.getImage();
            var designTemplateImagePresigned = s3Service.getPresignedUrl(designTemplateImageKey, S3ImageDuration.DESIGN_TEMPLATE_IMAGE_DURATION);
            designTemplateDTOResponse.setImage(designTemplateImagePresigned);
        }
        return designTemplateDTOResponse;
    }
}
