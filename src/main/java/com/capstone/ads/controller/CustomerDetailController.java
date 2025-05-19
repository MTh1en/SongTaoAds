package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customerDetail.CustomerDetailDTO;
import com.capstone.ads.dto.customerDetail.CustomerDetailRequestDTO;
import com.capstone.ads.service.CustomerDetailService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer-details")
@RequiredArgsConstructor
public class CustomerDetailController {

    private final CustomerDetailService customerDetailService;

    @PostMapping("/create")
    public ApiResponse<CustomerDetailDTO> createCustomerDetail(@RequestBody CustomerDetailRequestDTO request) {
        CustomerDetailDTO response = customerDetailService.createCustomerDetail(request);
        return ApiResponseBuilder.buildSuccessResponse("CustomerDetail created successfully", response);
    }

    @GetMapping("/{id}")
    public ApiResponse<CustomerDetailDTO> getCustomerDetailById(@PathVariable String id) {
        CustomerDetailDTO response = customerDetailService.getCustomerDetailById(id);
        return ApiResponseBuilder.buildSuccessResponse("CustomerDetail retrieved successfully", response);
    }

    @GetMapping("/getAll")
    public ApiResponse<List<CustomerDetailDTO>> getAllCustomerDetails() {
        List<CustomerDetailDTO> response = customerDetailService.getAllCustomerDetails();
        return ApiResponseBuilder.buildSuccessResponse("CustomerDetails retrieved successfully", response);
    }

    @PutMapping("/{id}")
    public ApiResponse<CustomerDetailDTO> updateCustomerDetail(@PathVariable String id, @RequestBody CustomerDetailRequestDTO request) {
        CustomerDetailDTO response = customerDetailService.updateCustomerDetail(id, request);
        return ApiResponseBuilder.buildSuccessResponse("CustomerDetail updated successfully", response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerDetail(@PathVariable String id) {
        customerDetailService.deleteCustomerDetail(id);
        return ResponseEntity.ok().build();
    }
}