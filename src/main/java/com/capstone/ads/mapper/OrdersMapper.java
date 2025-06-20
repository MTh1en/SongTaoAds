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
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(initOrderStatus())")
    @Mapping(target = "orderDate", expression = "java(initOrderDate())")
    @Mapping(target = "aiDesigns", ignore = true)
    @Mapping(target = "customDesignRequests", source = "customDesignRequests")
    @Mapping(target = "depositAmount", ignore = true)
    @Mapping(target = "remainingAmount", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    Orders toEntityFromCreateOrderByCustomDesign(CustomDesignRequests customDesignRequests, Users users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(initOrderStatus())")
    @Mapping(target = "orderDate", expression = "java(initOrderDate())")
    @Mapping(target = "aiDesigns", source = "aiDesigns")
    @Mapping(target = "customDesignRequests", ignore = true)
    @Mapping(target = "depositAmount", ignore = true)
    @Mapping(target = "remainingAmount", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    Orders toEntityFromCreateOrderByAIDesign(AIDesigns aiDesigns, Users users);

    OrderDTO toDTO(Orders order);

    void updateEntityFromUpdateInformationRequest(OrderUpdateAddressRequest request, @MappingTarget Orders orders);

    void updateEntityFromConfirmRequest(OrderConfirmRequest request, @MappingTarget Orders orders);

    default LocalDateTime initOrderDate() {
        return LocalDateTime.now();
    }

    default OrderStatus initOrderStatus() {
        return OrderStatus.PENDING_CONTRACT;
    }
}