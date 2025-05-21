package com.capstone.ads.mapper;

import com.capstone.ads.dto.customerDetail.CustomerDetailDTO;
import com.capstone.ads.dto.customerDetail.CustomerDetailRequestDTO;
import com.capstone.ads.model.AIDesigns;
import com.capstone.ads.model.CustomerDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerDetailMapper {
    CustomerDetailMapper INSTANCE = Mappers.getMapper(CustomerDetailMapper.class);

    @Mapping(source = "users.id", target = "userId")
    CustomerDetailDTO toDTO(CustomerDetail customerDetail);

    @Mapping(source = "userId", target = "users.id")
    CustomerDetail toEntity(CustomerDetailRequestDTO request);
    @Mapping(source = "userId", target = "users.id")
    @Mapping(target = "users", ignore = true) // Ignore users field during update
    void updateEntityFromDTO(CustomerDetailRequestDTO request, @MappingTarget CustomerDetail customerDetail);

}
