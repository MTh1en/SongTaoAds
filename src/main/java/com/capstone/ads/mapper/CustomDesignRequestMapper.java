package com.capstone.ads.mapper;

import com.capstone.ads.dto.customdesignrequest.CustomDesignRequestCreateRequest;
import com.capstone.ads.dto.customdesignrequest.CustomDesignRequestDTO;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.CustomerDetail;
import com.capstone.ads.model.Users;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface CustomDesignRequestMapper {
    @Mapping(target = "assignDesigner", source = "assignDesigner.id")
    @Mapping(target = "customerDetail", source = "customerDetail.id")
    CustomDesignRequestDTO toDTO(CustomDesignRequests customDesignRequests);

    @Mapping(target = "assignDesigner", ignore = true)
    @Mapping(target = "customerDetail", expression = "java(mapToCustomerDetail(customerDetailId))")
    @Mapping(target = "createdAt", expression = "java(initCreateAt())")
    @Mapping(target = "status", expression = "java(initStatus())")
    CustomDesignRequests toEntity(CustomDesignRequestCreateRequest request, String customerDetailId);

    default Users maptoUsers(String designerId) {
        if (designerId == null) return null;
        return Users.builder()
                .id(designerId)
                .build();
    }

    default CustomerDetail mapToCustomerDetail(String customerDetailId) {
        if (customerDetailId == null) return null;
        return CustomerDetail.builder()
                .id(customerDetailId)
                .build();
    }

    default LocalDateTime initCreateAt() {
        return LocalDateTime.now();
    }

    default CustomDesignRequestStatus initStatus() {
        return CustomDesignRequestStatus.PENDING;
    }
}
