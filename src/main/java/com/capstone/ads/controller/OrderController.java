package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderCreateRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateAddressRequest;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.model.enums.OrderType;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "ORDER")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping("/orders")
    public ApiResponse<OrderDTO> createOrder(@RequestBody OrderCreateRequest request) {
        var response = orderService.createOrder(request);
        return ApiResponseBuilder.buildSuccessResponse("Tạo đơn hàng thành công", response);
    }

    @PatchMapping("/orders/{orderId}/contract-resign")
    @Operation(summary = "Sale yêu cầu khách hàng gửi lại bạn hợp đồng đã ký")
    public ApiResponse<OrderDTO> saleRequestCustomerResignContract(@PathVariable String orderId) {
        var response = orderService.saleRequestCustomerResignContract(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Yêu cầu khách hàng gửi lại hợp đồng đã ký thành công", response);
    }

    @PatchMapping("/orders/{orderId}/contract-signed")
    @Operation(summary = "Sale xác nhận đơn hàng đã ký")
    public ApiResponse<OrderDTO> saleConfirmContractSigned(@PathVariable String orderId) {
        var response = orderService.saleConfirmContractSigned(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Xác nhận đơn hàng đã ký thành công", response);
    }

    @PatchMapping("/orders/{orderId}/address")
    @Operation(summary = "Khách hàng cung cấp địa chỉ giao hàng")
    public ApiResponse<OrderDTO> customerProvideAddress(@PathVariable String orderId,
                                                        @Valid @RequestBody OrderUpdateAddressRequest request) {
        var response = orderService.customerProvideAddress(orderId, request);
        return ApiResponseBuilder.buildSuccessResponse("Cung cấp địa chỉ giao hàng thành công", response);
    }

    @PatchMapping("/orders/{orderId}/estimate-delivery-date")
    @Operation(summary = "Sale báo ngày giao dự tính")
    public ApiResponse<OrderDTO> saleNotifyEstimateDeliveryDate(@PathVariable String orderId,
                                                                @RequestBody OrderConfirmRequest request) {
        var response = orderService.saleNotifyEstimateDeliveryDate(orderId, request);
        return ApiResponseBuilder.buildSuccessResponse("Báo ngày giao và đơn vị thi công thành công", response);
    }

    @GetMapping("/orders/{orderId}")
    @Operation(summary = "Xem order theo ID")
    public ApiResponse<OrderDTO> findOrderById(@PathVariable String orderId) {
        var response = orderService.findOrderById(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Xem order theo ID thành công", response);
    }

    @GetMapping("/users/{userId}/orders")
    @Operation(summary = "Xem order theo ID người dùng")
    public ApiPagingResponse<OrderDTO> findOrderByUserId(
            @PathVariable String userId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = orderService.findOrderByUserId(userId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem order theo thông tin người dùng", response, page);
    }

    @GetMapping(value = "/orders", params = {"orderStatus", "orderType"})
    @Operation(summary = "Xem order theo status và type")
    public ApiPagingResponse<OrderDTO> findOrderByStatusAndType(
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestParam(required = false) OrderType orderType,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = orderService.findOrderByStatusAndType(orderStatus, orderType, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem order theo trạng thái và loại thành công", response, page);
    }

    @GetMapping(value = "/orders", params = {"orderStatus", "!orderType"})
    @Operation(summary = "Xem order theo status")
    public ApiPagingResponse<OrderDTO> findOrderByStatus(
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = orderService.findOrderByStatus(orderStatus, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem order theo trạng thái thành công", response, page);
    }

    @GetMapping(value = "/orders", params = {"orderType", "!orderStatus"})
    @Operation(summary = "Xem order theo type")
    public ApiPagingResponse<OrderDTO> findOrderByType(
            @RequestParam(required = false) OrderType orderType,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = orderService.findOrderByType(orderType, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem order theo loại thành công", response, page);
    }

    @GetMapping(value = "/orders", params = {"!orderStatus", "!orderType"})
    @Operation(summary = "Xem tất cả order")
    public ApiPagingResponse<OrderDTO> findAllOrders(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = orderService.findAllOrders(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem tất cả order thành công", response, page);
    }


    @GetMapping(value = "/orders/custom-design", params = {"orderStatus"})
    @Operation(summary = "Xem tất cả order custom design theo trạng thái")
    public ApiPagingResponse<OrderDTO> findCustomDesignOrderByAndStatus(
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = orderService.findCustomDesignOrderByAndStatus(orderStatus, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem tất cả order custom theo trạng thái", response, page);
    }

    @GetMapping(value = "/orders/custom-design", params = {"!orderStatus"})
    @Operation(summary = "Xem tất cả order custom design")
    public ApiPagingResponse<OrderDTO> findCustomDesignOrderByAndStatus(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = orderService.findCustomDesignOrder(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Xem tất cả order custom thành công", response, page);
    }

    @PatchMapping("/orders/{orderId}/cancel")
    @Operation(summary = "Hủy đơn hàng")
    public ApiResponse<Void> cancelOrder(@PathVariable String orderId) {
        orderService.cancelOrder(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Hủy đơn hàng thành công", null);
    }

    @DeleteMapping("/orders/{orderId}")
    @Operation(summary = "Xóa cứng order (không cần dùng)")
    public ApiResponse<Void> hardDeleteOrder(@PathVariable String orderId) {
        orderService.hardDeleteOrder(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Xóa đơn hàng thành công", null);
    }
}