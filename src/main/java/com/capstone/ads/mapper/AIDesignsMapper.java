package com.capstone.ads.mapper;

import com.capstone.ads.dto.aidesign.AIDesignDTO;
import com.capstone.ads.model.AIDesigns;
import com.capstone.ads.model.CustomerDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AIDesignsMapper {
    @Mapping(target = "customerDetail", source = "customerDetail.id")
    AIDesignDTO toDTO(AIDesigns aidesigns);

    @Mapping(target = "customerDetail", expression = "java(mapToCustomerDetail(customerDetailId))")
    @Mapping(target = "customerNote", expression = "java(customerNote)")
    AIDesigns toEntity(String customerDetailId, String customerNote);

    default CustomerDetail mapToCustomerDetail(String customerDetailId) {
        if (customerDetailId == null) return null;
        return CustomerDetail.builder()
                .id(customerDetailId)
                .build();
    }

}
