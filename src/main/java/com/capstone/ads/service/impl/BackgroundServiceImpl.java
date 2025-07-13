package com.capstone.ads.service.impl;

import com.capstone.ads.dto.background.BackgroundCreateRequest;
import com.capstone.ads.dto.background.BackgroundDTO;
import com.capstone.ads.dto.background.BackgroundUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.BackgroundMapper;
import com.capstone.ads.model.AttributeValues;
import com.capstone.ads.model.Backgrounds;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.repository.internal.BackgroundsRepository;
import com.capstone.ads.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackgroundServiceImpl implements BackgroundService {
    private final CustomerChoicesService customerChoicesService;
    private final AttributeValuesService attributeValuesService;
    private final FileDataService fileDataService;
    private final BackgroundMapper backgroundMapper;
    private final BackgroundsRepository backgroundsRepository;

    @Override
    @Transactional
    public BackgroundDTO createBackgroundByAttributeValue(String attributeValueId, BackgroundCreateRequest request) {
        AttributeValues attributeValues = attributeValuesService.getAttributeValueById(attributeValueId);
        String backgroundImageUrl = uploadBackgroundImageToS3(attributeValueId, request.getBackgroundImage());

        Backgrounds backgrounds = backgroundMapper.mapCreateRequestToEntity(request);
        backgrounds.setAttributeValues(attributeValues);
        backgrounds.setBackgroundUrl(backgroundImageUrl);
        backgrounds = backgroundsRepository.save(backgrounds);

        return backgroundMapper.toDTO(backgrounds);
    }

    @Override
    @Transactional
    public BackgroundDTO updateBackgroundInformation(String backgroundId, BackgroundUpdateRequest request) {
        Backgrounds backgrounds = getAvailableBackgroundById(backgroundId);

        backgroundMapper.mapUpdateRequestToEntity(request, backgrounds);
        backgrounds = backgroundsRepository.save(backgrounds);

        return backgroundMapper.toDTO(backgrounds);
    }

    @Override
    @Transactional
    public BackgroundDTO updateBackgroundImage(String backgroundId, MultipartFile backgroundImage) {
        Backgrounds backgrounds = getAvailableBackgroundById(backgroundId);
        fileDataService.hardDeleteFileDataByImageUrl(backgrounds.getBackgroundUrl());

        String backgroundImageUrl = uploadBackgroundImageToS3(backgrounds.getAttributeValues().getId(), backgroundImage);
        backgrounds.setBackgroundUrl(backgroundImageUrl);
        backgrounds = backgroundsRepository.save(backgrounds);
        
        return backgroundMapper.toDTO(backgrounds);
    }

    @Override
    public List<BackgroundDTO> suggestedBackgrounds(String customerChoiceId) {
        CustomerChoices customerChoices = customerChoicesService.getCustomerChoiceById(customerChoiceId);

        Set<String> attributeValueIds = customerChoices.getCustomerChoiceDetails().stream()
                .map(detail -> detail.getAttributeValues().getId())
                .collect(Collectors.toSet());

        return backgroundsRepository.findByAttributeValues_IdInAndIsAvailable(attributeValueIds, true)
                .stream()
                .map(backgroundMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BackgroundDTO> findBackgroundByAttributeValue(String attributeValueId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return backgroundsRepository.findByAttributeValues_IdAndIsAvailable(attributeValueId, true, pageable)
                .map(backgroundMapper::toDTO);
    }

    @Override
    public Page<BackgroundDTO> findAllBackground(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return backgroundsRepository.findAll(pageable)
                .map(backgroundMapper::toDTO);
    }

    @Override
    @Transactional
    public void hardDeleteBackground(String backgroundId) {
        if (!backgroundsRepository.existsById(backgroundId)) {
            throw new AppException(ErrorCode.BACKGROUND_NOT_FOUND);
        }
        backgroundsRepository.deleteById(backgroundId);
    }

    //INTERNAL FUNCTION//

    @Override
    public Backgrounds getAvailableBackgroundById(String backgroundId) {
        return backgroundsRepository.findByIdAndIsAvailable(backgroundId, true)
                .orElseThrow(() -> new AppException(ErrorCode.BACKGROUND_NOT_FOUND));
    }

    private String generateBackgroundKey(String attributeValueId) {
        return String.format("background/%s/%s", attributeValueId, UUID.randomUUID());
    }

    private String uploadBackgroundImageToS3(String attributeValueId, MultipartFile file) {
        String backgroundImageKey = generateBackgroundKey(attributeValueId);
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        fileDataService.uploadSingleFile(backgroundImageKey, file);
        return backgroundImageKey;
    }
}
