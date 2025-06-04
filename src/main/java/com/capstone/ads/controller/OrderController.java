package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateInformationRequest;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping("/custom-designs/{customDesignId}/orders")
    public ApiResponse<OrderDTO> createOrderByCustomDesign(@PathVariable String customDesignId) {
        var response = service.createOrderByCustomDesign(customDesignId);
        return ApiResponseBuilder.buildSuccessResponse("Create order successful", response);
    }

    @PostMapping("/ai-designs/{aiDesignId}/customer-choices/{customerChoiceId}/orders")
    public ApiResponse<OrderDTO> createOrderByAIDesign(@PathVariable String aiDesignId,
                                                       @PathVariable String customerChoiceId) {
        var response = service.createOrderByAIDesign(customerChoiceId, aiDesignId);
        return ApiResponseBuilder.buildSuccessResponse("Create order successful", response);
    }

    @PatchMapping("/orders/{orderId}/customer-information")
    public ApiResponse<OrderDTO> update(@PathVariable String orderId, @RequestBody OrderUpdateInformationRequest request) {
        var response = service.customerUpdateOrderInformation(orderId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update order information successful", response);
    }

    @PatchMapping("/orders/{orderId}/sale-confirm")
    public ApiResponse<OrderDTO> update(@PathVariable String orderId, @RequestBody OrderConfirmRequest request) {
        var response = service.saleConfirmOrder(orderId, request);
        return ApiResponseBuilder.buildSuccessResponse("Confirm order successful", response);
    }

    @PatchMapping("/orders/{orderId}/status")
    public ApiResponse<OrderDTO> update(@PathVariable String orderId, @RequestParam OrderStatus status) {
        var response = service.changeOrderStatus(orderId, status);
        return ApiResponseBuilder.buildSuccessResponse("Confirm order successful", response);
    }

    @GetMapping("/orders/{orderId}")
    public ApiResponse<OrderDTO> findOrderById(@PathVariable String orderId) {
        var response = service.findOrderById(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Find Order by Id", response);
    }

    @GetMapping("/users/{userId}/orders")
    public ApiResponse<List<OrderDTO>> getByUserId(@PathVariable String userId) {
        var response = service.findOrderByUserId(userId);
        return ApiResponseBuilder.buildSuccessResponse("Find Order by UserId: ", response);
    }

    @GetMapping("/orders")
    public ApiResponse<List<OrderDTO>> findOrderByStatus(@RequestParam OrderStatus orderStatus) {
        var response = service.findOrderByStatus(orderStatus);
        return ApiResponseBuilder.buildSuccessResponse("Find Order By Status", response);
    }

    @DeleteMapping("/orders/{orderId}")
    public ApiResponse<Void> hardDeleteOrder(@PathVariable String orderId) {
        service.hardDeleteOrder(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Hard delete order successful", null);
    }
}