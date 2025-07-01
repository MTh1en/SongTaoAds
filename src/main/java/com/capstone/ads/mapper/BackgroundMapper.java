package com.capstone.ads.mapper;

import com.capstone.ads.dto.background.BackgroundCreateRequest;
import com.capstone.ads.dto.background.BackgroundDTO;
import com.capstone.ads.dto.background.BackgroundUpdateRequest;
import com.capstone.ads.model.Backgrounds;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BackgroundMapper {
    BackgroundDTO toDTO(Backgrounds backgrounds);

    @Mapping(target = "isAvailable", expression = "java(initIsAvailable())")
    Backgrounds mapCreateRequestToEntity(BackgroundCreateRequest backgroundCreateRequest);

    void mapUpdateRequestToEntity(BackgroundUpdateRequest request, @MappingTarget Backgrounds backgrounds);

    default Boolean initIsAvailable() {
        return true;
    }
}
