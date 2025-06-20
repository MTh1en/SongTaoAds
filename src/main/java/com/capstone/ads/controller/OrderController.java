package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateAddressRequest;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/custom-design-request/{customDesignRequestId}/customer-choices/{customerChoiceId}/orders")
    public ApiResponse<OrderDTO> createOrderByCustomDesign(@PathVariable String customDesignRequestId,
                                                           @PathVariable String customerChoiceId) {
        var response = orderService.createOrderByCustomDesign(customDesignRequestId, customerChoiceId);
        return ApiResponseBuilder.buildSuccessResponse("Create order successful", response);
    }

    @PostMapping("/ai-designs/{aiDesignId}/customer-choices/{customerChoiceId}/orders")
    public ApiResponse<OrderDTO> createOrderByAIDesign(@PathVariable String aiDesignId,
                                                       @PathVariable String customerChoiceId) {
        var response = orderService.createOrderByAIDesign(aiDesignId, customerChoiceId);
        return ApiResponseBuilder.buildSuccessResponse("Create order successful", response);
    }

    @PatchMapping("/orders/{orderId}/contract-signed")
    public ApiResponse<OrderDTO> saleConfirmContractSigned(@PathVariable String orderId) {
        var response = orderService.saleConfirmContractSigned(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Sale confirm contract signed", response);
    }

    @PatchMapping(value = "/orders/{orderId}/producing", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<OrderDTO> saleConfirmContractSigned(@PathVariable String orderId,
                                                           @RequestPart MultipartFile draftImage) {
        var response = orderService.managerUpdateOrderProducing(orderId, draftImage);
        return ApiResponseBuilder.buildSuccessResponse("Manager update Order Producing", response);
    }

    @PatchMapping(value = "/orders/{orderId}/production-completed", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<OrderDTO> managerUpdateOrderProductionComplete(@PathVariable String orderId,
                                                                      @RequestPart MultipartFile productImage) {
        var response = orderService.managerUpdateOrderProductionComplete(orderId, productImage);
        return ApiResponseBuilder.buildSuccessResponse("Manager update Order Production Completed", response);
    }

    @PatchMapping(value = "/orders/{orderId}/delivering", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<OrderDTO> managerUpdateOrderDelivering(@PathVariable String orderId,
                                                              @RequestPart MultipartFile deliveryImage) {
        var response = orderService.managerUpdateOrderDelivering(orderId, deliveryImage);
        return ApiResponseBuilder.buildSuccessResponse("Manager update Order Delivering", response);
    }

    @PatchMapping(value = "/orders/{orderId}/installed", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<OrderDTO> manageUpdateOrderInstalled(@PathVariable String orderId,
                                                            @RequestPart MultipartFile installedImage) {
        var response = orderService.manageUpdateOrderInstalled(orderId, installedImage);
        return ApiResponseBuilder.buildSuccessResponse("Manager update Order Installed", response);
    }

    @PatchMapping("/orders/{orderId}/address")
    public ApiResponse<OrderDTO> customerProvideAddress(@PathVariable String orderId, @RequestBody OrderUpdateAddressRequest request) {
        var response = orderService.customerProvideAddress(orderId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update order information successful", response);
    }

    @PatchMapping("/orders/{orderId}/estimate-delivery-date")
    public ApiResponse<OrderDTO> saleNotifyEstimateDeliveryDate(@PathVariable String orderId, @RequestBody OrderConfirmRequest request) {
        var response = orderService.saleNotifyEstimateDeliveryDate(orderId, request);
        return ApiResponseBuilder.buildSuccessResponse("Confirm order successful", response);
    }

    @GetMapping("/orders/{orderId}")
    public ApiResponse<OrderDTO> findOrderById(@PathVariable String orderId) {
        var response = orderService.findOrderById(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Find Order by Id", response);
    }

    @GetMapping("/users/{userId}/orders")
    public ApiPagingResponse<OrderDTO> findOrderByUserId(@PathVariable String userId,
                                                         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                         @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = orderService.findOrderByUserId(userId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find Order by Users", response, page);
    }

    @GetMapping("/orders")
    public ApiPagingResponse<OrderDTO> findOrderByStatus(@RequestParam OrderStatus orderStatus,
                                                         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                         @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = orderService.findOrderByStatus(orderStatus, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find Order By Status", response, page);
    }

    @DeleteMapping("/orders/{orderId}")
    public ApiResponse<Void> hardDeleteOrder(@PathVariable String orderId) {
        orderService.hardDeleteOrder(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Hard hardDeleteAttribute order successful", null);
    }
}