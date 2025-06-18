package com.capstone.ads.mapper;

import com.capstone.ads.dto.demo_design.DemoDesignDTO;
import com.capstone.ads.dto.demo_design.CustomerDecisionCustomDesignRequest;
import com.capstone.ads.dto.demo_design.DesignerUpdateDescriptionCustomDesignRequest;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.DemoDesigns;
import com.capstone.ads.model.enums.DemoDesignStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DemoDesignsMapper {
    @Mapping(target = "customDesignRequests", source = "customDesignRequests.id")
    DemoDesignDTO toDTO(DemoDesigns demoDesigns);

    @Mapping(target = "customDesignRequests", expression = "java(mapToCustomDesignRequests(customerDesignRequestId))")
    @Mapping(target = "status", expression = "java(initStatus())")
    @Mapping(target = "designerDescription", expression = "java(designerDescription)")
    DemoDesigns toEntity(String designerDescription, String customerDesignRequestId);

    void updateEntityFromCustomerRequest(CustomerDecisionCustomDesignRequest request, @MappingTarget DemoDesigns demoDesigns);

    void updateEntityFromDesignerRequest(DesignerUpdateDescriptionCustomDesignRequest request, @MappingTarget DemoDesigns demoDesigns);

    default CustomDesignRequests mapToCustomDesignRequests(String customerDesignRequestId) {
        if (customerDesignRequestId == null) return null;
        return CustomDesignRequests.builder().id(customerDesignRequestId).build();
    }

    default DemoDesignStatus initStatus() {
        return DemoDesignStatus.PENDING;
    }
}
