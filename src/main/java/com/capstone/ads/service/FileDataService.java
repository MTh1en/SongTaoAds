package com.capstone.ads.service;

import com.capstone.ads.dto.file.FileDataDTO;
import com.capstone.ads.dto.file.IconCreateRequest;
import com.capstone.ads.dto.file.IconUpdateInfoRequest;
import com.capstone.ads.model.FileData;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface FileDataService {
    FileDataDTO uploadIconSystem(IconCreateRequest request);

    FileDataDTO updateIconSystemInformation(String iconId, IconUpdateInfoRequest request);

    FileDataDTO updateIconSystemImage(String iconId, MultipartFile iconImage);

    Page<FileDataDTO> findAllIconSystem(int page, int size);

    void deleteIconSystem(String iconId);

    //INTERNAL FUNCTION//

    FileData getFileDataById(String fileId);
}
