package com.capstone.ads.mapper;

import com.capstone.ads.dto.customer_detail.CustomerDetailDTO;
import com.capstone.ads.dto.customer_detail.CustomerDetailRequest;
import com.capstone.ads.model.CustomerDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerDetailMapper {
    CustomerDetailDTO toDTO(CustomerDetail customerDetail);

    CustomerDetail toEntity(String companyName, String tagLine, String contactInfo);

    @Mapping(target = "users", ignore = true)
    void updateEntityFromDTO(CustomerDetailRequest request, @MappingTarget CustomerDetail customerDetail);
}
