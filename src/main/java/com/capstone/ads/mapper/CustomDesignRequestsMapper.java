package com.capstone.ads.mapper;

import com.capstone.ads.dto.custom_design_request.CustomDesignRequestCreateRequest;
import com.capstone.ads.dto.custom_design_request.CustomDesignRequestDTO;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.CustomerDetail;
import com.capstone.ads.model.Users;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface CustomDesignRequestsMapper {
    CustomDesignRequestDTO toDTO(CustomDesignRequests customDesignRequests);

    CustomDesignRequests mapCreateRequestToEntity(CustomDesignRequestCreateRequest request);
}
