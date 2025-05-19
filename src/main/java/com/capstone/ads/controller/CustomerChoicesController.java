package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customerchoice.CustomerChoicesCreateRequest;
import com.capstone.ads.dto.customerchoice.CustomerChoicesDTO;
import com.capstone.ads.dto.customerchoice.CustomerChoicesUpdateRequest;
import com.capstone.ads.service.CustomerChoicesService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerChoicesController {
    private final CustomerChoicesService service;

    @PostMapping("/product-types/{productTypeId}/customer-choices")
    public ApiResponse<CustomerChoicesDTO> create(@PathVariable String productTypeId,
                                                  @RequestBody CustomerChoicesCreateRequest request) {
        return ApiResponseBuilder.buildSuccessResponse("Create attribute successful", service.create(productTypeId, request));
    }

    @PutMapping("/customer-choices/{customerChoicesId}")
    public ApiResponse<CustomerChoicesDTO> update(@PathVariable String customerChoicesId,
                                                  @RequestBody CustomerChoicesUpdateRequest request) {
        return ApiResponseBuilder.buildSuccessResponse("Update attribute successful", service.update(customerChoicesId, request));
    }

    @GetMapping("/customer-choices/{customerChoicesId}")
    public ApiResponse<CustomerChoicesDTO> getById(@PathVariable String customerChoicesId) {
        return ApiResponseBuilder.buildSuccessResponse("attribute by Id", service.findById(customerChoicesId));
    }

    @GetMapping("/customers/{customerId}/customer-choices")
    public ApiResponse<List<CustomerChoicesDTO>> getNewestByCustomer(@PathVariable String customerId) {
        return ApiResponseBuilder.buildSuccessResponse("Find all attribute by product type", service.findNewestByUserId(customerId));
    }

    @DeleteMapping("/customer-choices/{customerChoicesId}")
    public ApiResponse<Void> delete(@PathVariable String customerChoicesId) {
        service.delete(customerChoicesId);
        return ApiResponseBuilder.buildSuccessResponse("Delete attribute successful", null);
    }
}
