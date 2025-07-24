package com.capstone.ads.mapper;

import com.capstone.ads.dto.order_detail.OrderDetailCreateRequest;
import com.capstone.ads.dto.order_detail.OrderDetailDTO;
import com.capstone.ads.model.OrderDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailDTO toDTO(OrderDetails orderDetails);

    OrderDetails mapCreateRequestToEntity(OrderDetailCreateRequest request);
}
