package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateInformationRequest;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping("/custom-design-request/{customDesignRequestId}/customer-choices/{customerChoiceId}/orders")
    public ApiResponse<OrderDTO> createOrderByCustomDesign(@PathVariable String customDesignRequestId,
                                                           @PathVariable String customerChoiceId) {
        var response = service.createOrderByCustomDesign(customDesignRequestId, customerChoiceId);
        return ApiResponseBuilder.buildSuccessResponse("Create order successful", response);
    }

    @PostMapping("/ai-designs/{aiDesignId}/customer-choices/{customerChoiceId}/orders")
    public ApiResponse<OrderDTO> createOrderByAIDesign(@PathVariable String aiDesignId,
                                                       @PathVariable String customerChoiceId) {
        var response = service.createOrderByAIDesign(aiDesignId, customerChoiceId);
        return ApiResponseBuilder.buildSuccessResponse("Create order successful", response);
    }

    @PatchMapping("/orders/{orderId}/customer-information")
    public ApiResponse<OrderDTO> update(@PathVariable String orderId, @RequestBody OrderUpdateInformationRequest request) {
        var response = service.customerProvideAddress(orderId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update order information successful", response);
    }

    @PatchMapping("/orders/{orderId}/sale-confirm")
    public ApiResponse<OrderDTO> update(@PathVariable String orderId, @RequestBody OrderConfirmRequest request) {
        var response = service.saleNotifyEstimateDeliveryDate(orderId, request);
        return ApiResponseBuilder.buildSuccessResponse("Confirm order successful", response);
    }

    @GetMapping("/orders/{orderId}")
    public ApiResponse<OrderDTO> findOrderById(@PathVariable String orderId) {
        var response = service.findOrderById(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Find Order by Id", response);
    }

    @GetMapping("/users/{userId}/orders")
    public ApiPagingResponse<OrderDTO> findOrderByUserId(@PathVariable String userId,
                                                         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                         @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findOrderByUserId(userId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find Order by Users", response, page);
    }

    @GetMapping("/orders")
    public ApiPagingResponse<OrderDTO> findOrderByStatus(@RequestParam OrderStatus orderStatus,
                                                         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                         @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = service.findOrderByStatus(orderStatus, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find Order By Status", response, page);
    }

    @DeleteMapping("/orders/{orderId}")
    public ApiResponse<Void> hardDeleteOrder(@PathVariable String orderId) {
        service.hardDeleteOrder(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Hard hardDeleteAttribute order successful", null);
    }
}