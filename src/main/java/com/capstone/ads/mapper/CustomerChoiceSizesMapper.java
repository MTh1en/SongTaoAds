package com.capstone.ads.mapper;

import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeCreateRequest;
import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeDTO;
import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeUpdateRequest;
import com.capstone.ads.model.CustomerChoiceSizes;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerChoiceSizesMapper {
    CustomerChoicesSizeDTO toDTO(CustomerChoiceSizes customerChoiceSizes);

    CustomerChoiceSizes mapCreateRequestToEntity(CustomerChoicesSizeCreateRequest request);

    void updateEntityFromRequest(CustomerChoicesSizeUpdateRequest request, @MappingTarget CustomerChoiceSizes customerChoiceSizes);
}
