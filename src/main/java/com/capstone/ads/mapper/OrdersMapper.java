package com.capstone.ads.mapper;

import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateInformationRequest;
import com.capstone.ads.model.*;
import com.capstone.ads.model.enums.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface OrdersMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(initStatus())")
    @Mapping(target = "orderDate", expression = "java(initOrderDate())")
    Orders toEntityFromCreateOrderByCustomDesign(CustomDesignRequests customDesignRequests, Users users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(initStatus())")
    @Mapping(target = "orderDate", expression = "java(initOrderDate())")
    Orders toEntityFromCreateOrderByAIDesign(AIDesigns aiDesigns, Users users);

    OrderDTO toDTO(Orders order);

    void updateEntityFromUpdateInformationRequest(OrderUpdateInformationRequest request, @MappingTarget Orders orders);

    void updateEntityFromConfirmRequest(OrderConfirmRequest request, @MappingTarget Orders orders);

    default LocalDateTime initOrderDate() {
        return LocalDateTime.now();
    }

    default OrderStatus initStatus() {
        return OrderStatus.PENDING;
    }
}