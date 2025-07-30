package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageKeyFormat;
import com.capstone.ads.dto.file.*;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.FileDataMapper;
import com.capstone.ads.model.*;
import com.capstone.ads.model.enums.FileTypeEnum;
import com.capstone.ads.repository.internal.FileDataRepository;
import com.capstone.ads.service.*;
import io.micrometer.common.util.StringUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileDataServiceImpl implements FileDataService {
    S3Service s3Service;
    FileDataMapper fileDataMapper;
    FileDataRepository fileDataRepository;

    @Override
    public FileDataDTO uploadSingleFile(String key, MultipartFile file) {
        String imageUrl = s3Service.uploadSingleFile(key, file);

        FileData fileData = FileData.builder()
                .imageUrl(imageUrl)
                .fileType(FileTypeEnum.LOG)
                .fileSize(file.getSize())
                .contentType(file.getContentType())
                .build();
        fileData = fileDataRepository.save(fileData);

        return fileDataMapper.toDTO(fileData);
    }

    @Override
    public FileDataDTO findFileDataByImageUrl(String imageUrl) {
        FileData fileData = fileDataRepository.findByImageUrl(imageUrl)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_FOUND));
        return fileDataMapper.toDTO(fileData);
    }

    @Override
    @Transactional
    public FileDataDTO uploadIconSystem(IconCreateRequest request) {
        MultipartFile iconFile = request.getIconImage();
        String iconUrl = uploadIconToS3(iconFile);

        FileData fileData = fileDataMapper.mapCreateRequestToEntity(request);
        fileData.setImageUrl(iconUrl);
        fileData.setFileType(FileTypeEnum.ICON);
        fileData.setFileSize(iconFile.getSize());
        fileData.setContentType(iconFile.getContentType());
        fileData = fileDataRepository.save(fileData);

        return fileDataMapper.toDTO(fileData);
    }

    @Override
    @Transactional
    public FileDataDTO updateIconSystemInformation(String iconId, IconUpdateInfoRequest request) {
        FileData fileData = getFileDataById(iconId);

        fileDataMapper.mapUpdateRequestToEntity(request, fileData);
        fileData = fileDataRepository.save(fileData);
        return fileDataMapper.toDTO(fileData);
    }

    @Override
    @Transactional
    public FileDataDTO updateIconSystemImage(String iconId, MultipartFile iconImage) {
        FileData fileData = getFileDataById(iconId);
        String iconUrl = uploadIconToS3(iconImage);

        fileData.setImageUrl(iconUrl);
        fileData.setContentType(iconImage.getContentType());
        fileData.setFileSize(iconImage.getSize());
        fileData = fileDataRepository.save(fileData);

        return fileDataMapper.toDTO(fileData);
    }

    @Override
    public Page<FileDataDTO> findAllIconSystem(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return fileDataRepository.findByFileType(FileTypeEnum.ICON, pageable)
                .map(fileDataMapper::toDTO);
    }

    @Override
    @Transactional
    public void hardDeleteFileDataById(String fileDataId) {
        FileData fileData = getFileDataById(fileDataId);
        s3Service.deleteFile(fileData.getImageUrl());
        fileDataRepository.deleteById(fileDataId);
    }

    @Override
    public void hardDeleteFileDataByImageUrl(String imageUrl) {
        if (!StringUtils.isBlank(imageUrl)) {
            s3Service.deleteFile(imageUrl);
            fileDataRepository.deleteByImageUrl(imageUrl);
        }
    }

    @Override
    public List<FileDataDTO> findFileDataByDemoDesignId(String demoDesignId) {
        return fileDataRepository.findByDemoDesigns_Id(demoDesignId).stream()
                .map(fileDataMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FileDataDTO> findFileDataByCustomDesignRequestId(String customDesignRequestId) {
        return fileDataRepository.findByCustomDesignRequests_Id(customDesignRequestId).stream()
                .map(fileDataMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FileDataDTO> findFileDataByProgressLogId(String progressLogId) {
        return fileDataRepository.findByProgressLogs_Id(progressLogId).stream()
                .map(fileDataMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FileData getFileDataById(String fileId) {
        return fileDataRepository.findById(fileId)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_FOUND));
    }


    private String generateIconKey() {
        return String.format(S3ImageKeyFormat.ICON, UUID.randomUUID());
    }

    private String uploadIconToS3(MultipartFile iconImage) {
        String iconKey = generateIconKey();
        return s3Service.uploadSingleFile(iconKey, iconImage);
    }

    @Override
    public <T> List<FileDataDTO> uploadMultipleFiles(
            List<MultipartFile> files,
            FileTypeEnum fileType,
            T entity,
            BiConsumer<FileData, T> entitySetter,
            BiFunction<String, Integer, List<String>> keyGenerator
    ) {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("File list cannot be null or empty");
        }

        String entityId = switch (entity) {
            case Orders orders -> orders.getId();
            case DemoDesigns demoDesigns -> demoDesigns.getId();
            case CustomDesignRequests customDesignRequests -> customDesignRequests.getId();
            case ProgressLogs progressLogs -> progressLogs.getId();
            default ->
                    throw new IllegalArgumentException("Unsupported entity type for file upload: " + entity.getClass().getName());
        };

        List<FileData> savedFiles = new ArrayList<>();
        List<String> formattedKeys = keyGenerator.apply(entityId, files.size());
        List<String> uploadedKeys = s3Service.uploadMultipleFiles(files, formattedKeys);

        IntStream.range(0, files.size()).forEach(i -> {
            if (uploadedKeys.contains(formattedKeys.get(i))) {
                FileData fileData = FileData.builder()
                        .fileType(fileType)
                        .fileSize(files.get(i).getSize())
                        .contentType(files.get(i).getContentType() != null
                                ? files.get(i).getContentType()
                                : "application/octet-stream")
                        .imageUrl(formattedKeys.get(i))
                        .build();
                entitySetter.accept(fileData, entity);
                savedFiles.add(fileData);
            }
        });

        fileDataRepository.saveAll(savedFiles);
        return savedFiles.stream()
                .map(fileDataMapper::toDTO)
                .collect(Collectors.toList());
    }
}
