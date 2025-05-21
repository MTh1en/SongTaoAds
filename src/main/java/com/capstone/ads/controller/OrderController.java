package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderCreateRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateRequest;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ApiResponse<OrderDTO> create(@RequestBody OrderCreateRequest request) {
        return ApiResponseBuilder.buildSuccessResponse("Create order successful", orderService.createOrder(request));
    }

    @PutMapping("/orders/{orderId}")
    public ApiResponse<OrderDTO> update(@PathVariable String orderId, @RequestBody OrderUpdateRequest request) {
        return ApiResponseBuilder.buildSuccessResponse("Update order successful", orderService.updateOrder(orderId, request));
    }

    @GetMapping("/orders/{orderId}")
    public ApiResponse<OrderDTO> getById(@PathVariable String orderId) {
        return ApiResponseBuilder.buildSuccessResponse("Order by Id", orderService.getOrderById(orderId));
    }
    @GetMapping("/users/{userId}/orders")
    public ApiResponse<List<OrderDTO>> getByUserId(@PathVariable String userId) {
        return ApiResponseBuilder.buildSuccessResponse("Order by user: ", orderService.getOrderByUserId(userId));
    }

    @GetMapping("/orders")
    public ApiResponse<List<OrderDTO>> getAll() {
        return ApiResponseBuilder.buildSuccessResponse("Find all orders", orderService.getAllOrders());
    }

    @DeleteMapping("/orders/{orderId}")
    public ApiResponse<Void> delete(@PathVariable String orderId) {
        orderService.deleteOrder(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Delete order successful", null);
    }
    @PostMapping("/orders/{orderId}/confirm")
    public ApiResponse<OrderDTO> confirm(@PathVariable String orderId, @RequestBody OrderConfirmRequest request) {
        return ApiResponseBuilder.buildSuccessResponse("Confirm order successful", orderService.confirmOrder(orderId, request));
    }
}