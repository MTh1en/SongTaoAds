package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.order_detail.OrderDetailCreateRequest;
import com.capstone.ads.dto.order_detail.OrderDetailDTO;
import com.capstone.ads.service.OrderDetailService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "ORDER DETAIL")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailController {
    OrderDetailService orderDetailService;

    @PostMapping("/orders/{orderId}/order-details")
    public ApiResponse<OrderDetailDTO> createOrderDetail(@PathVariable("orderId") String orderId,
                                                         @RequestBody OrderDetailCreateRequest request) {
        var response = orderDetailService.createOrderDetail(orderId, request);
        return ApiResponseBuilder.buildSuccessResponse("Order Detail Created", response);
    }
}
