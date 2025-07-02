package com.capstone.ads.mapper;

import com.capstone.ads.dto.file.FileDataDTO;
import com.capstone.ads.dto.file.IconCreateRequest;
import com.capstone.ads.dto.file.IconUpdateInfoRequest;
import com.capstone.ads.model.FileData;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FileDataMapper {
    FileDataDTO toDTO(FileData fileData);

    FileData mapCreateRequestToEntity(IconCreateRequest iconCreateRequest);

    void mapUpdateRequestToEntity(IconUpdateInfoRequest request, @MappingTarget FileData fileData);
}
