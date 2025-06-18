package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customer_choice.CustomerChoicesDTO;
import com.capstone.ads.service.CustomerChoicesService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerChoicesController {
    private final CustomerChoicesService service;

    @PostMapping("/customers/{customerId}/product-types/{productTypeId}")
    public ApiResponse<CustomerChoicesDTO> createCustomerChoice(@PathVariable String customerId, @PathVariable String productTypeId) {
        var response = service.createCustomerChoice(customerId, productTypeId);
        return ApiResponseBuilder.buildSuccessResponse("Create customer choices successful", response);
    }

    @GetMapping("/customer-choices/{customerChoicesId}")
    public ApiResponse<CustomerChoicesDTO> findCustomerChoiceById(@PathVariable String customerChoicesId) {
        var response = service.findCustomerChoiceById(customerChoicesId);
        return ApiResponseBuilder.buildSuccessResponse("customer choices by Id", response);
    }

    @GetMapping("/customers/{customerId}/customer-choices")
    public ApiResponse<CustomerChoicesDTO> findCustomerChoiceByUserId(@PathVariable String customerId) {
        var response = service.findCustomerChoiceByUserId(customerId);
        return ApiResponseBuilder.buildSuccessResponse("Find all customer choices by product type", response);
    }

    @DeleteMapping("/customer-choices/{customerChoicesId}")
    public ApiResponse<Void> hardDeleteCustomerChoice(@PathVariable String customerChoicesId) {
        service.hardDeleteCustomerChoice(customerChoicesId);
        return ApiResponseBuilder.buildSuccessResponse("Delete customer choices successful", null);
    }
}
