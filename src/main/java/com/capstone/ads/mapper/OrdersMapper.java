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
    @Mapping(target = "status", expression = "java(initCustomOrderStatus())")
    @Mapping(target = "orderDate", expression = "java(initOrderDate())")
    @Mapping(target = "editedDesigns", ignore = true)
    @Mapping(target = "customDesignRequests", source = "customDesignRequests")
    @Mapping(target = "depositAmount", ignore = true)
    @Mapping(target = "remainingAmount", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "users", source = "users")
    Orders toEntityFromCreateOrderByCustomDesign(CustomDesignRequests customDesignRequests, Users users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(initAIOrderStatus())")
    @Mapping(target = "orderDate", expression = "java(initOrderDate())")
    @Mapping(target = "editedDesigns", source = "editedDesigns")
    @Mapping(target = "customDesignRequests", ignore = true)
    @Mapping(target = "depositAmount", ignore = true)
    @Mapping(target = "remainingAmount", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "users", source = "users")
    Orders toEntityFromCreateOrderByAIDesign(EditedDesigns editedDesigns, Users users);

    OrderDTO toDTO(Orders order);

    void updateEntityFromUpdateInformationRequest(OrderUpdateAddressRequest request, @MappingTarget Orders orders);

    void updateEntityFromConfirmRequest(OrderConfirmRequest request, @MappingTarget Orders orders);

    default LocalDateTime initOrderDate() {
        return LocalDateTime.now();
    }

    default OrderStatus initAIOrderStatus() {
        return OrderStatus.PENDING_CONTRACT;
    }

    default OrderStatus initCustomOrderStatus() {
        return OrderStatus.PENDING_DESIGN;
    }
}