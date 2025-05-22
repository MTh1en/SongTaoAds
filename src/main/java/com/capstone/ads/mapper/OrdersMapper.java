package com.capstone.ads.mapper;

import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateRequest;
import com.capstone.ads.model.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrdersMapper {
    OrderDTO toDTO(Orders order);

    void updateEntityFromDTO(OrderUpdateRequest updateDTO, @MappingTarget Orders orders);
}