package com.capstone.ads.mapper;

import com.capstone.ads.dto.demo_design.CustomerRejectCustomDesignRequest;
import com.capstone.ads.dto.demo_design.DemoDesignCreateRequest;
import com.capstone.ads.dto.demo_design.DemoDesignDTO;
import com.capstone.ads.dto.demo_design.DesignerUpdateDescriptionCustomDesignRequest;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.DemoDesigns;
import com.capstone.ads.model.enums.DemoDesignStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DemoDesignsMapper {
    DemoDesignDTO toDTO(DemoDesigns demoDesigns);

    DemoDesigns mapCreateRequestToEntity(DemoDesignCreateRequest request);

    void updateEntityFromCustomerRequest(CustomerRejectCustomDesignRequest request, @MappingTarget DemoDesigns demoDesigns);

    void updateEntityFromDesignerRequest(DesignerUpdateDescriptionCustomDesignRequest request, @MappingTarget DemoDesigns demoDesigns);
}
