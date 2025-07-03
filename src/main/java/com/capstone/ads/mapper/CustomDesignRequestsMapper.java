package com.capstone.ads.mapper;

import com.capstone.ads.dto.custom_design_request.CustomDesignRequestCreateRequest;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestDTO;
import com.capstone.ads.model.CustomDesignRequests;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomDesignRequestsMapper {
    CustomDesignRequestDTO toDTO(CustomDesignRequests customDesignRequests);

    CustomDesignRequests mapCreateRequestToEntity(CustomDesignRequestCreateRequest request);
}
