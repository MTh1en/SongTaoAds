package com.capstone.ads.mapper;

import com.capstone.ads.dto.customer_choice_detail.CustomerChoicesDetailsDTO;
import com.capstone.ads.model.AttributeValues;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.model.CustomerChoiceDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerChoiceDetailsMapper {
    CustomerChoicesDetailsDTO toDTO(CustomerChoiceDetails customerChoiceDetails);
}
