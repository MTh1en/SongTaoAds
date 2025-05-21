package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customerDetail.CustomerDetailDTO;
import com.capstone.ads.dto.customerDetail.CustomerDetailRequestDTO;
import com.capstone.ads.service.CustomerDetailService;
import com.capstone.ads.utils.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/customer-details")
@RequiredArgsConstructor
public class CustomerDetailController {

    private final CustomerDetailService customerDetailService;

    @PostMapping
    public ApiResponse<CustomerDetailDTO> create(@Valid @RequestBody CustomerDetailRequestDTO request) {
        return ApiResponseBuilder.buildSuccessResponse("Create customer detail successful", customerDetailService.createCustomerDetail(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<CustomerDetailDTO> getById(@PathVariable String id) {
        return ApiResponseBuilder.buildSuccessResponse("Customer detail by ID", customerDetailService.getCustomerDetailById(id));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<CustomerDetailDTO> getByUserId(@PathVariable String userId) {
        return ApiResponseBuilder.buildSuccessResponse("Customer detail by user ID", customerDetailService.getCustomerDetailByUserId(userId));
    }

    @GetMapping
    public ApiResponse<List<CustomerDetailDTO>> getAll() {
        return ApiResponseBuilder.buildSuccessResponse("Find all customer details", customerDetailService.getAllCustomerDetails());
    }

    @PutMapping("/{id}")
    public ApiResponse<CustomerDetailDTO> update(@PathVariable String id, @Valid @RequestBody CustomerDetailRequestDTO request) {
        return ApiResponseBuilder.buildSuccessResponse("Update customer detail successful", customerDetailService.updateCustomerDetail(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        customerDetailService.deleteCustomerDetail(id);
        return ApiResponseBuilder.buildSuccessResponse("Delete customer detail successful", null);
    }
}