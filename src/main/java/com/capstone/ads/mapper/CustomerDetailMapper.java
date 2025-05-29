package com.capstone.ads.mapper;

import com.capstone.ads.dto.customerDetail.CustomerDetailDTO;
import com.capstone.ads.dto.customerDetail.CustomerDetailRequest;
import com.capstone.ads.model.CustomerDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerDetailMapper {
    @Mapping(source = "users.id", target = "userId")
    CustomerDetailDTO toDTO(CustomerDetail customerDetail);

    CustomerDetail toEntity(String companyName, String tagLine, String contactInfo);

    @Mapping(target = "users", ignore = true)
    void updateEntityFromDTO(CustomerDetailRequest request, @MappingTarget CustomerDetail customerDetail);

}
