package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customer_choice.CustomerChoicesDTO;
import com.capstone.ads.service.CustomerChoicesService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "CUSTOMER CHOICE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerChoicesController {
    CustomerChoicesService service;

    @PostMapping("/customers/{customerId}/product-types/{productTypeId}")
    @Operation(summary = "Tạo loại sản phẩm khách hàng chọn")
    public ApiResponse<CustomerChoicesDTO> createCustomerChoice(@PathVariable String customerId,
                                                                @PathVariable String productTypeId) {
        var response = service.createCustomerChoice(customerId, productTypeId);
        return ApiResponseBuilder.buildSuccessResponse("Create customer choices successful", response);
    }

    @GetMapping("/customer-choices/{customerChoicesId}")
    @Operation(summary = "Xem loại sản phẩm khách hàng chọn theo ID")
    public ApiResponse<CustomerChoicesDTO> findCustomerChoiceById(@PathVariable String customerChoicesId) {
        var response = service.findCustomerChoiceById(customerChoicesId);
        return ApiResponseBuilder.buildSuccessResponse("customer choices by Id", response);
    }

    @GetMapping("/customers/{customerId}/customer-choices")
    @Operation(summary = "Xem loại sản phẩm khách hàng chọn theo ID khách hàng")
    public ApiResponse<CustomerChoicesDTO> findCustomerChoiceByUserId(@PathVariable String customerId) {
        var response = service.findCustomerChoiceByUserId(customerId);
        return ApiResponseBuilder.buildSuccessResponse("Find all customer choices by product type", response);
    }

    @DeleteMapping("/customer-choices/{customerChoicesId}")
    @Operation(summary = "Xóa cứng loại sản phẩm khách hàng chọn")
    public ApiResponse<Void> hardDeleteCustomerChoice(@PathVariable String customerChoicesId) {
        service.hardDeleteCustomerChoice(customerChoicesId);
        return ApiResponseBuilder.buildSuccessResponse("Delete customer choices successful", null);
    }
}
