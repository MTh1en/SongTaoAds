package com.capstone.ads.mapper;

import com.capstone.ads.dto.size.SizeCreateRequest;
import com.capstone.ads.dto.size.SizeDTO;
import com.capstone.ads.dto.size.SizeUpdateRequest;
import com.capstone.ads.model.Sizes;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SizesMapper {
    SizeDTO toDTO(Sizes sizes);

    Sizes mapCreateRequestToEntity(SizeCreateRequest request);

    void updateEntityFromRequest(SizeUpdateRequest request, @MappingTarget Sizes sizes);
}
