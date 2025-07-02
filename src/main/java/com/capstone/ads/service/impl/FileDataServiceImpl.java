package com.capstone.ads.service.impl;

import com.capstone.ads.dto.file.FileDataDTO;
import com.capstone.ads.dto.file.IconCreateRequest;
import com.capstone.ads.dto.file.IconUpdateInfoRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.FileDataMapper;
import com.capstone.ads.model.FileData;
import com.capstone.ads.repository.internal.FileDataRepository;
import com.capstone.ads.service.FileDataService;
import com.capstone.ads.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileDataServiceImpl implements FileDataService {
    private final S3Service s3Service;
    private final FileDataMapper fileDataMapper;
    private final FileDataRepository fileDataRepository;

    @Override
    @Transactional
    public FileDataDTO uploadIconSystem(IconCreateRequest request) {
        MultipartFile iconFile = request.getIconImage();
        String iconUrl = uploadIconToS3(iconFile);

        FileData fileData = fileDataMapper.mapCreateRequestToEntity(request);
        fileData.setImageUrl(iconUrl);
        fileData.setFileType(iconFile.getContentType());
        fileData.setFileSize(iconFile.getSize());
        fileData.setContentType("ICON");
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
        fileData.setFileType(iconImage.getContentType());
        fileData.setFileSize(iconImage.getSize());
        fileData = fileDataRepository.save(fileData);

        return fileDataMapper.toDTO(fileData);
    }

    @Override
    public Page<FileDataDTO> findAllIconSystem(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return fileDataRepository.findByContentTypeIgnoreCase("ICON", pageable)
                .map(fileDataMapper::toDTO);
    }

    @Override
    @Transactional
    public void deleteIconSystem(String iconId) {
        if (!fileDataRepository.existsById(iconId)) {
            throw new AppException(ErrorCode.ICON_NOT_FOUND);
        }
        fileDataRepository.deleteById(iconId);
    }

    @Override
    public FileData getFileDataById(String fileId) {
        return fileDataRepository.findById(fileId)
                .orElseThrow(() -> new AppException(ErrorCode.ICON_NOT_FOUND));
    }


    private String generateIconKey() {
        return String.format("icon/%s", UUID.randomUUID());
    }

    private String uploadIconToS3(MultipartFile iconImage) {
        String iconKey = generateIconKey();
        return s3Service.uploadSingleFile(iconKey, iconImage);
    }
}
