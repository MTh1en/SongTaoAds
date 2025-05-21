package com.capstone.ads.mapper;

import com.capstone.ads.dto.order.OrderCreateRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateRequest;
import com.capstone.ads.model.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrdersMapper {

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "aiDesigns", ignore = true)
    @Mapping(target = "payments", ignore = true)

    Orders toEntity(OrderCreateRequest request);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "users", target = "user")
    @Mapping(source = "aiDesigns.id", target = "aiDesignId")
    OrderDTO toDTO(Orders order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "aiDesigns", ignore = true)
    void updateEntityFromDTO(OrderUpdateRequest updateDTO, @MappingTarget Orders orders);
}