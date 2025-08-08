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

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "ORDER DETAIL")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailController {
    OrderDetailService orderDetailService;

    @PostMapping("/orders/{orderId}/details")
    public ApiResponse<OrderDetailDTO> createOrderDetail(@PathVariable("orderId") String orderId,
                                                         @RequestBody OrderDetailCreateRequest request) {
        var response = orderDetailService.createOrderDetail(orderId, request);
        return ApiResponseBuilder.buildSuccessResponse("Tạo chi tiêt đơn hàng thành công", response);
    }

    @GetMapping("/orders/{orderId}/details")
    public ApiResponse<List<OrderDetailDTO>> getOrderDetail(@PathVariable("orderId") String orderId) {
        var response = orderDetailService.getOrderDetailsByOrderId(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Xem chi tiết đơn hàng thành công", response);
    }

    @DeleteMapping("/orders/{orderId}/details")
    public ApiResponse<Void> deleteOrderDetail(@PathVariable("orderId") String orderId) {
        orderDetailService.hardDeleteOrderDetail(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Xóa chi tiết đơn hàng", null);
    }
}
