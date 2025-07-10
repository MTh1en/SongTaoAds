package com.capstone.ads.mapper;

import com.capstone.ads.dto.demo_design.CustomerRejectCustomDesignRequest;
import com.capstone.ads.dto.demo_design.DemoDesignCreateRequest;
import com.capstone.ads.dto.demo_design.DemoDesignDTO;
import com.capstone.ads.dto.demo_design.DesignerUpdateDescriptionCustomDesignRequest;
import com.capstone.ads.model.DemoDesigns;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DemoDesignsMapper {
    DemoDesignDTO toDTO(DemoDesigns demoDesigns);

    DemoDesigns mapCreateRequestToEntity(DemoDesignCreateRequest request);

    void updateEntityFromCustomerRequest(CustomerRejectCustomDesignRequest request, @MappingTarget DemoDesigns demoDesigns);

    void updateEntityFromDesignerRequest(DesignerUpdateDescriptionCustomDesignRequest request, @MappingTarget DemoDesigns demoDesigns);
}
