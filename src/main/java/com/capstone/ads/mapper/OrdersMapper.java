package com.capstone.ads.mapper;

import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderCreateRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateAddressRequest;
import com.capstone.ads.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrdersMapper {
    OrderDTO toDTO(Orders order);

    Orders mapCreateRequestToEntity(OrderCreateRequest createRequest);

    void updateEntityFromUpdateInformationRequest(OrderUpdateAddressRequest request, @MappingTarget Orders orders);

    void updateEntityFromConfirmRequest(OrderConfirmRequest request, @MappingTarget Orders orders);
}