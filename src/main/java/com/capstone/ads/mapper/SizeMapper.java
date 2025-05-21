package com.capstone.ads.mapper;

import com.capstone.ads.dto.size.SizeCreateRequest;
import com.capstone.ads.dto.size.SizeDTO;
import com.capstone.ads.dto.size.SizeUpdateRequest;
import com.capstone.ads.model.Size;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SizeMapper {
    SizeDTO toDTO(Size size);

    Size toEntity(SizeCreateRequest request);

    void updateEntityFromRequest(SizeUpdateRequest request, @MappingTarget Size size);
}
