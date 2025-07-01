package com.capstone.ads.mapper;

import com.capstone.ads.dto.customer_choice.CustomerChoicesDTO;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.model.ProductTypes;
import com.capstone.ads.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerChoicesMapper {
    CustomerChoicesDTO toDTO(CustomerChoices customerChoices);
}
