package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.order.OrderConfirmDTO;
import com.capstone.ads.dto.order.OrderCreateDTO;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateDTO;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<OrderDTO> create(@RequestBody OrderCreateDTO request) {
        return ApiResponseBuilder.buildSuccessResponse("Create order successful", orderService.createOrder(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<OrderDTO> update(@PathVariable("id") String id, @RequestBody OrderUpdateDTO request) {
        return ApiResponseBuilder.buildSuccessResponse("Update order successful", orderService.updateOrder(id, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderDTO> getById(@PathVariable String id) {
        return ApiResponseBuilder.buildSuccessResponse("Order by Id", orderService.getOrderById(id));
    }

    @GetMapping
    public ApiResponse<List<OrderDTO>> getAll() {
        return ApiResponseBuilder.buildSuccessResponse("Find all orders", orderService.getAllOrders());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        orderService.deleteOrder(id);
        return ApiResponseBuilder.buildSuccessResponse("Delete order successful", null);
    }
    @PostMapping("/confirm/{id}")
    public ApiResponse<OrderDTO> confirm(@PathVariable String id, @RequestBody OrderConfirmDTO request) {
        return ApiResponseBuilder.buildSuccessResponse("Confirm order successful", orderService.confirmOrder(id, request));
    }
}