package com.capstone.ads.mapper;

import com.capstone.ads.dto.order.OrderCreateDTO;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateDTO;
import com.capstone.ads.model.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrdersMapper {

    @Mapping(source = "users.id", target = "userId")
    @Mapping(source = "aiDesigns.id", target = "aiDesignId")
    OrderDTO toDTO(Orders orders);

    @Mapping(source = "userId", target = "users.id")
    @Mapping(source = "aiDesignId", target = "aiDesigns.id")
    Orders toEntity(OrderCreateDTO createDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "aiDesigns", ignore = true)
    void updateEntityFromDTO(OrderUpdateDTO updateDTO, @MappingTarget Orders orders);
}