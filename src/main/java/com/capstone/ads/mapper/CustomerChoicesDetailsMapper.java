package com.capstone.ads.mapper;

import com.capstone.ads.dto.customerchoicedetail.CustomerChoicesDetailsDTO;
import com.capstone.ads.model.AttributeValues;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.model.CustomerChoicesDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerChoicesDetailsMapper {
    @Mapping(target = "customerChoicesId", source = "customerChoices.id")
    @Mapping(target = "attributeValuesId", source = "attributeValues.id")
    CustomerChoicesDetailsDTO toDTO(CustomerChoicesDetails customerChoicesDetails);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerChoices", expression = "java(mapCustomerChoices(customerChoicesId))")
    @Mapping(target = "attributeValues", expression = "java(mapAttributeValues(attributeValuesId))")
    CustomerChoicesDetails toEntity(String customerChoicesId, String attributeValuesId);

    default CustomerChoices mapCustomerChoices(String customerChoicesId) {
        if (customerChoicesId == null) return null;
        CustomerChoices customerChoices = new CustomerChoices();
        customerChoices.setId(customerChoicesId);
        return customerChoices;
    }

    default AttributeValues mapAttributeValues(String attributeValuesId) {
        if (attributeValuesId == null) return null;
        AttributeValues attributeValues = new AttributeValues();
        attributeValues.setId(attributeValuesId);
        return attributeValues;
    }
}
