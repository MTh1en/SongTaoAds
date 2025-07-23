package com.capstone.ads.service;

import com.capstone.ads.dto.file.*;
import com.capstone.ads.model.FileData;
import com.capstone.ads.model.enums.FileTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface FileDataService {
    FileDataDTO uploadSingleFile(String key, MultipartFile file);

    FileDataDTO findFileDataByImageUrl(String imageUrl);

    <T> List<FileDataDTO> uploadMultipleFiles(
            List<MultipartFile> files,
            FileTypeEnum fileType,
            T entity,
            BiConsumer<FileData, T> entitySetter,
            BiFunction<String, Integer, List<String>> keyGenerator
    );

    void hardDeleteFileDataById(String fileDataId);

    void hardDeleteFileDataByImageUrl(String imageUrl);

    // ===== ICON ===== ///
    FileDataDTO uploadIconSystem(IconCreateRequest request);

    FileDataDTO updateIconSystemInformation(String iconId, IconUpdateInfoRequest request);

    FileDataDTO updateIconSystemImage(String iconId, MultipartFile iconImage);

    Page<FileDataDTO> findAllIconSystem(int page, int size);

    // ===== DEMO DESIGN ===== //
    List<FileDataDTO> findFileDataByDemoDesignId(String demoDesignId);

    // ===== CUSTOM DESIGN REQUEST ===== //
    List<FileDataDTO> findFileDataByCustomDesignRequestId(String customDesignRequestId);

    //INTERNAL FUNCTION//
    FileData getFileDataById(String fileId);
}
