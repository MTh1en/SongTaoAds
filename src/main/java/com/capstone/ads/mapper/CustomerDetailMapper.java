package com.capstone.ads.mapper;

import com.capstone.ads.dto.customer_detail.CustomerDetailCreateRequest;
import com.capstone.ads.dto.customer_detail.CustomerDetailDTO;
import com.capstone.ads.dto.customer_detail.CustomerDetailUpdateRequest;
import com.capstone.ads.model.CustomerDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerDetailMapper {
    CustomerDetailDTO toDTO(CustomerDetail customerDetail);

    CustomerDetail mapCreateRequestToEntity(CustomerDetailCreateRequest request);

    @Mapping(target = "users", ignore = true)
    void updateEntityFromDTO(CustomerDetailUpdateRequest request, @MappingTarget CustomerDetail customerDetail);
}
