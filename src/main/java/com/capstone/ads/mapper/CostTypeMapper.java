package com.capstone.ads.mapper;

import com.capstone.ads.dto.cost_type.CostTypeCreateRequest;
import com.capstone.ads.dto.cost_type.CostTypeDTO;
import com.capstone.ads.dto.cost_type.CostTypeUpdateRequest;
import com.capstone.ads.model.CostTypes;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CostTypeMapper {
    CostTypeDTO toDTO(CostTypes costTypes);

    CostTypes mapCreateRequestToEntity(CostTypeCreateRequest request);

    void mapUpdateRequestToEntity(CostTypeUpdateRequest request, @MappingTarget CostTypes costTypes);
}
