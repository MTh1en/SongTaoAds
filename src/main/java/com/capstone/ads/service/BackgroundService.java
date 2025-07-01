package com.capstone.ads.service;

import com.capstone.ads.dto.background.BackgroundCreateRequest;
import com.capstone.ads.dto.background.BackgroundDTO;
import com.capstone.ads.dto.background.BackgroundUpdateRequest;
import com.capstone.ads.model.Backgrounds;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface BackgroundService {
    BackgroundDTO createBackgroundByAttributeValue(String attributeValueId, BackgroundCreateRequest request);

    BackgroundDTO updateBackgroundInformation(String backgroundId, BackgroundUpdateRequest request);

    BackgroundDTO updateBackgroundImage(String backgroundId, MultipartFile backgroundImage);

    Page<BackgroundDTO> findBackgroundByAttributeValue(String attributeValueId, int page, int size);

    Page<BackgroundDTO> findAllBackground(int page, int size);

    void hardDeleteBackground(String backgroundId);

    //INTERNAL FUNCTION
    Backgrounds getAvailableBackgroundById(String backgroundId);
}
