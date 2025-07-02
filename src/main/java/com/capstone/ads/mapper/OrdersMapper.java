package com.capstone.ads.mapper;

import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateAddressRequest;
import com.capstone.ads.model.*;
import com.capstone.ads.model.enums.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface OrdersMapper {
    OrderDTO toDTO(Orders order);

    void updateEntityFromUpdateInformationRequest(OrderUpdateAddressRequest request, @MappingTarget Orders orders);

    void updateEntityFromConfirmRequest(OrderConfirmRequest request, @MappingTarget Orders orders);
}