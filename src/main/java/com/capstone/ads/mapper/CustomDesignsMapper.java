package com.capstone.ads.mapper;

import com.capstone.ads.dto.customdesign.CustomDesignDTO;
import com.capstone.ads.dto.customdesign.CustomerDecisionCustomDesignRequest;
import com.capstone.ads.dto.customdesign.DesignerUpdateDescriptionCustomDesignRequest;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.CustomDesigns;
import com.capstone.ads.model.enums.CustomDesignStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomDesignsMapper {
    @Mapping(target = "customDesignRequests", source = "customDesignRequests.id")
    CustomDesignDTO toDTO(CustomDesigns customDesigns);

    @Mapping(target = "customDesignRequests", expression = "java(mapToCustomDesignRequests(customerDesignRequestId))")
    @Mapping(target = "status", expression = "java(initStatus())")
    @Mapping(target = "designerDescription", expression = "java(designerDescription)")
    CustomDesigns toEntity(String designerDescription, String customerDesignRequestId);

    void updateEntityFromCustomerRequest(CustomerDecisionCustomDesignRequest request, @MappingTarget CustomDesigns customDesigns);

    void updateEntityFromDesignerRequest(DesignerUpdateDescriptionCustomDesignRequest request, @MappingTarget CustomDesigns customDesigns);

    default CustomDesignRequests mapToCustomDesignRequests(String customerDesignRequestId) {
        if (customerDesignRequestId == null) return null;
        return CustomDesignRequests.builder().id(customerDesignRequestId).build();
    }

    default CustomDesignStatus initStatus() {
        return CustomDesignStatus.PENDING;
    }
}
