package com.capstone.ads.mapper;

import com.capstone.ads.dto.customer_choice_detail.CustomerChoicesDetailsDTO;
import com.capstone.ads.model.CustomerChoiceDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerChoiceDetailsMapper {
    CustomerChoicesDetailsDTO toDTO(CustomerChoiceDetails customerChoiceDetails);
}
