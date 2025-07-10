package com.capstone.ads.mapper;

import com.capstone.ads.dto.customer_choice.CustomerChoicesDTO;
import com.capstone.ads.model.CustomerChoices;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerChoicesMapper {
    CustomerChoicesDTO toDTO(CustomerChoices customerChoices);
}
