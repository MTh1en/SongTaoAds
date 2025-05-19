package com.capstone.ads.mapper;

import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeCreateRequest;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeDTO;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeUpdateRequest;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.model.CustomerChoicesSize;
import com.capstone.ads.model.Size;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerChoicesSizeMapper {
    @Mapping(target = "customerChoicesId", source = "customerChoices.id")
    @Mapping(target = "sizeId", source = "size.id")
    CustomerChoicesSizeDTO toDTO(CustomerChoicesSize customerChoicesSize);

    @Mapping(target = "customerChoices", expression = "java(mapCustomerChoices(customerChoicesId))")
    @Mapping(target = "size", expression = "java(mapSize(sizeId))")
    CustomerChoicesSize toEntity(CustomerChoicesSizeCreateRequest request, String customerChoicesId, String sizeId);

    void updateEntityFromRequest(CustomerChoicesSizeUpdateRequest request, @MappingTarget CustomerChoicesSize customerChoicesSize);

    default CustomerChoices mapCustomerChoices(String customerChoicesId) {
        if (customerChoicesId == null) return null;
        CustomerChoices customerChoices = new CustomerChoices();
        customerChoices.setId(customerChoicesId);
        return customerChoices;
    }

    default Size mapSize(String sizeId) {
        if (sizeId == null) return null;
        Size size = new Size();
        size.setId(sizeId);
        return size;
    }
}
