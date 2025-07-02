package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageDuration;
import com.capstone.ads.dto.edited_design.EditedDesignCreateRequest;
import com.capstone.ads.dto.edited_design.EditedDesignDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.EditedDesignMapper;
import com.capstone.ads.model.Backgrounds;
import com.capstone.ads.model.CustomerDetail;
import com.capstone.ads.model.DesignTemplates;
import com.capstone.ads.model.EditedDesigns;
import com.capstone.ads.repository.internal.EditedDesignsRepository;
import com.capstone.ads.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EditedDesignServiceImpl implements EditedDesignService {
    private final CustomerDetailService customerDetailService;
    private final DesignTemplatesService designTemplatesService;
    private final BackgroundService backgroundService;
    private final S3Service s3Service;
    private final EditedDesignsRepository editedDesignsRepository;
    private final EditedDesignMapper editedDesignMapper;

    @Override
    @Transactional
    public EditedDesignDTO createEditedDesignFromDesignTemplate(String customerDetailId, String designTemplateId,
                                                                EditedDesignCreateRequest request) {
        CustomerDetail customerDetail = customerDetailService.getCustomerChoiceDetailById(customerDetailId);
        DesignTemplates designTemplates = designTemplatesService.getDesignTemplateById(designTemplateId);
        String aiDesignImageUrl = uploadAIDesignImageToS3(customerDetailId, request.getEditedImage());

        EditedDesigns editedDesigns = editedDesignMapper.mapCreateRequestToEntity(request);
        editedDesigns.setCustomerDetail(customerDetail);
        editedDesigns.setDesignTemplates(designTemplates);
        editedDesigns.setEditedImage(aiDesignImageUrl);

        editedDesigns = editedDesignsRepository.save(editedDesigns);
        return editedDesignMapper.toDTO(editedDesigns);
    }

    @Override
    @Transactional
    public EditedDesignDTO createEditedDesignFromBackground(String customerDetailId, String backgroundId,
                                                            EditedDesignCreateRequest request) {
        CustomerDetail customerDetail = customerDetailService.getCustomerChoiceDetailById(customerDetailId);
        Backgrounds backgrounds = backgroundService.getAvailableBackgroundById(backgroundId);
        String aiDesignImageUrl = uploadAIDesignImageToS3(customerDetailId, request.getEditedImage());

        EditedDesigns editedDesigns = editedDesignMapper.mapCreateRequestToEntity(request);
        editedDesigns.setCustomerDetail(customerDetail);
        editedDesigns.setBackgrounds(backgrounds);
        editedDesigns.setEditedImage(aiDesignImageUrl);

        editedDesigns = editedDesignsRepository.save(editedDesigns);
        return editedDesignMapper.toDTO(editedDesigns);
    }

    @Override
    public Page<EditedDesignDTO> findEditedDesignByCustomerDetailId(String customerDetailId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return editedDesignsRepository.findByCustomerDetail_Id(customerDetailId, pageable)
                .map(editedDesignMapper::toDTO);
    }

    @Override
    @Transactional
    public void hardDeleteEditedDesign(String AIDesignId) {
        if (!editedDesignsRepository.existsById(AIDesignId)) {
            throw new AppException(ErrorCode.AI_DESIGN_NOT_FOUND);
        }
        editedDesignsRepository.deleteById(AIDesignId);
    }

    @Override
    public EditedDesigns getEditedDesignById(String AIDesignId) {
        return editedDesignsRepository.findById(AIDesignId)
                .orElseThrow(() -> new AppException(ErrorCode.AI_DESIGN_NOT_FOUND));
    }

    private EditedDesignDTO convertToAIDesignDTOWithImageIsPresignedURL(EditedDesigns editedDesigns) {
        var aiDesignDTOResponse = editedDesignMapper.toDTO(editedDesigns);
        if (!Objects.isNull(editedDesigns.getEditedImage())) {
            var designTemplateImagePresigned = s3Service.getPresignedUrl(editedDesigns.getEditedImage(), S3ImageDuration.CUSTOM_DESIGN_DURATION);
            aiDesignDTOResponse.setEditedImage(designTemplateImagePresigned);
        }
        return aiDesignDTOResponse;
    }

    private String generateAIDesignKey(String customerDetailId) {
        return String.format("ai-designs/%s/%s", customerDetailId, UUID.randomUUID());
    }

    private String uploadAIDesignImageToS3(String customerDetailId, MultipartFile file) {
        String AIDesignImageKey = generateAIDesignKey(customerDetailId);
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        s3Service.uploadSingleFile(AIDesignImageKey, file);
        return AIDesignImageKey;
    }
}
