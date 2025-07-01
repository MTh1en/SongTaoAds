package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateAddressRequest;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "ORDER")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/custom-design-request/{customDesignRequestId}/orders")
    @Operation(
            summary = "Tạo đơn hàng theo custom request",
            description = "Sau khi khách hàng hoàn thành thiết kế tùy chỉnh làm màn hình cho khách hàng chọn," +
                    "muốn tạo order thì call api này không thì thôi"
    )
    public ApiResponse<OrderDTO> createOrderByCustomDesign(@PathVariable String customDesignRequestId) {
        var response = orderService.createOrderByCustomDesign(customDesignRequestId);
        return ApiResponseBuilder.buildSuccessResponse("Create order successful", response);
    }

    @PostMapping("/ai-designs/{aiDesignId}/customer-choices/{customerChoiceId}/orders")
    @Operation(summary = "Tạo đơn hàng theo thiết kế AI")
    public ApiResponse<OrderDTO> createOrderByAIDesign(@PathVariable String aiDesignId,
                                                       @PathVariable String customerChoiceId) {
        var response = orderService.createOrderByAIDesign(aiDesignId, customerChoiceId);
        return ApiResponseBuilder.buildSuccessResponse("Create order successful", response);
    }

    @PatchMapping("/orders/{orderId}/contract-resign")
    @Operation(summary = "Sale yêu cầu khách hàng gửi lại bạn hợp đồng đã ký")
    public ApiResponse<OrderDTO> saleRequestCustomerResignContract(@PathVariable String orderId) {
        var response = orderService.saleRequestCustomerResignContract(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Sale request customer resign contract", response);
    }

    @PatchMapping("/orders/{orderId}/contract-signed")
    @Operation(summary = "Sale xác nhận đơn hàng đã ký")
    public ApiResponse<OrderDTO> saleConfirmContractSigned(@PathVariable String orderId) {
        var response = orderService.saleConfirmContractSigned(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Sale confirm contract signed", response);
    }

    @PatchMapping("/orders/{orderId}/address")
    @Operation(summary = "Khách hàng cung cấp địa chỉ giao hàng")
    public ApiResponse<OrderDTO> customerProvideAddress(@PathVariable String orderId,
                                                        @RequestBody OrderUpdateAddressRequest request) {
        var response = orderService.customerProvideAddress(orderId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update order information successful", response);
    }

    @PatchMapping("/orders/{orderId}/estimate-delivery-date")
    @Operation(summary = "Sale báo ngày giao dự tính")
    public ApiResponse<OrderDTO> saleNotifyEstimateDeliveryDate(@PathVariable String orderId,
                                                                @RequestBody OrderConfirmRequest request) {
        var response = orderService.saleNotifyEstimateDeliveryDate(orderId, request);
        return ApiResponseBuilder.buildSuccessResponse("Confirm order successful", response);
    }

    @PatchMapping(value = "/orders/{orderId}/producing", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Staff cập nhật đơn hàng bắt đầu làm")
    public ApiResponse<OrderDTO> managerUpdateOrderProducing(@PathVariable String orderId,
                                                             @RequestPart MultipartFile draftImage) {
        var response = orderService.staffUpdateOrderProducing(orderId, draftImage);
        return ApiResponseBuilder.buildSuccessResponse("Manager update Order Producing", response);
    }

    @PatchMapping(value = "/orders/{orderId}/production-completed", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Staff cập nhật đơn hàng đã làm xong")
    public ApiResponse<OrderDTO> managerUpdateOrderProductionComplete(@PathVariable String orderId,
                                                                      @RequestPart MultipartFile productImage) {
        var response = orderService.staffUpdateOrderProductionComplete(orderId, productImage);
        return ApiResponseBuilder.buildSuccessResponse("Manager update Order Production Completed", response);
    }

    @PatchMapping(value = "/orders/{orderId}/delivering", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Staff cập nhật đơn hàng đang vận chuyển")
    public ApiResponse<OrderDTO> managerUpdateOrderDelivering(@PathVariable String orderId,
                                                              @RequestPart MultipartFile deliveryImage) {
        var response = orderService.staffUpdateOrderDelivering(orderId, deliveryImage);
        return ApiResponseBuilder.buildSuccessResponse("Manager update Order Delivering", response);
    }

    @PatchMapping(value = "/orders/{orderId}/installed", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Staff cập nhật đơn hàng đã lắp đặt xong")
    public ApiResponse<OrderDTO> manageUpdateOrderInstalled(@PathVariable String orderId,
                                                            @RequestPart MultipartFile installedImage) {
        var response = orderService.staffUpdateOrderInstalled(orderId, installedImage);
        return ApiResponseBuilder.buildSuccessResponse("Manager update Order Installed", response);
    }

    @GetMapping("/orders/{orderId}")
    @Operation(summary = "Xem order theo ID")
    public ApiResponse<OrderDTO> findOrderById(@PathVariable String orderId) {
        var response = orderService.findOrderById(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Find Order by Id", response);
    }

    @GetMapping("/users/{userId}/orders")
    @Operation(summary = "Xem order theo ID người dùng")
    public ApiPagingResponse<OrderDTO> findOrderByUserId(
            @PathVariable String userId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = orderService.findOrderByUserId(userId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find Order by Users", response, page);
    }

    @GetMapping("/orders")
    @Operation(summary = "Xem order theo status")
    public ApiPagingResponse<OrderDTO> findOrderByStatus(
            @RequestParam OrderStatus orderStatus,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var response = orderService.findOrderByStatus(orderStatus, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Find Order By Status", response, page);
    }

    @DeleteMapping("/orders/{orderId}")
    @Operation(summary = "Xóa cứng order (không cần dùng)")
    public ApiResponse<Void> hardDeleteOrder(@PathVariable String orderId) {
        orderService.hardDeleteOrder(orderId);
        return ApiResponseBuilder.buildSuccessResponse("Hard hardDeleteAttribute order successful", null);
    }
}