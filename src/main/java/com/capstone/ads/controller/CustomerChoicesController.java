package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customerchoice.CustomerChoicesDTO;
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
    public ApiResponse<CustomerChoicesDTO> create(@PathVariable String customerId, @PathVariable String productTypeId) {
        return ApiResponseBuilder.buildSuccessResponse("Create customer choices successful", service.create(customerId, productTypeId));
    }

    @PutMapping("/customers/{customerId}/product-types/{productTypeId}")
    public ApiResponse<CustomerChoicesDTO> update(@PathVariable String customerId, @PathVariable String productTypeId) {
        return ApiResponseBuilder.buildSuccessResponse("Finish customer choices successful", service.finish(customerId, productTypeId));
    }

    @GetMapping("/customer-choices/{customerChoicesId}")
    public ApiResponse<CustomerChoicesDTO> getById(@PathVariable String customerChoicesId) {
        return ApiResponseBuilder.buildSuccessResponse("customer choices by Id", service.findById(customerChoicesId));
    }

    @GetMapping("/customers/{customerId}/customer-choices")
    public ApiResponse<CustomerChoicesDTO> getNewestByCustomer(@PathVariable String customerId) {
        return ApiResponseBuilder.buildSuccessResponse("Find all customer choices by product type", service.findNewestByUserId(customerId));
    }

    @DeleteMapping("/customer-choices/{customerChoicesId}")
    public ApiResponse<Void> delete(@PathVariable String customerChoicesId) {
        service.delete(customerChoicesId);
        return ApiResponseBuilder.buildSuccessResponse("Delete customer choices successful", null);
    }
}
