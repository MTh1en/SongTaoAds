package com.capstone.ads.mapper;

import com.capstone.ads.dto.customer_choice_cost.CustomerChoiceCostDTO;
import com.capstone.ads.model.CustomerChoiceCosts;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerChoiceCostMapper {
    CustomerChoiceCostDTO toDTO(CustomerChoiceCosts customerChoiceCosts);
}
