package com.capstone.ads.mapper;

import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeCreateRequest;
import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeDTO;
import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeUpdateRequest;
import com.capstone.ads.model.CustomerChoiceSizes;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.model.Sizes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerChoiceSizesMapper {
    @Mapping(target = "customerChoicesId", source = "customerChoices.id")
    @Mapping(target = "sizeId", source = "sizes.id")
    CustomerChoicesSizeDTO toDTO(CustomerChoiceSizes customerChoiceSizes);

    @Mapping(target = "customerChoices", expression = "java(mapCustomerChoices(customerChoicesId))")
    @Mapping(target = "sizes", expression = "java(mapSize(sizeId))")
    CustomerChoiceSizes toEntity(CustomerChoicesSizeCreateRequest request, String customerChoicesId, String sizeId);

    void updateEntityFromRequest(CustomerChoicesSizeUpdateRequest request, @MappingTarget CustomerChoiceSizes customerChoiceSizes);

    default CustomerChoices mapCustomerChoices(String customerChoicesId) {
        if (customerChoicesId == null) return null;
        CustomerChoices customerChoices = new CustomerChoices();
        customerChoices.setId(customerChoicesId);
        return customerChoices;
    }

    default Sizes mapSize(String sizeId) {
        if (sizeId == null) return null;
        Sizes sizes = new Sizes();
        sizes.setId(sizeId);
        return sizes;
    }
}
