package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customerDetail.CustomerDetailDTO;
import com.capstone.ads.dto.customerDetail.CustomerDetailRequest;
import com.capstone.ads.service.CustomerDetailService;
import com.capstone.ads.utils.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerDetailController {

    private final CustomerDetailService customerDetailService;

    @PostMapping("/customer-details")
    public ApiResponse<CustomerDetailDTO> create(@Valid @RequestBody CustomerDetailRequest request) {
        return ApiResponseBuilder.buildSuccessResponse(
                "Create customer detail successful",
                customerDetailService.createCustomerDetail(request)
        );
    }

    @GetMapping("/customer-details/{customerDetailId}")
    public ApiResponse<CustomerDetailDTO> getById(@PathVariable("customerDetailId") String customerDetailId) {
        return ApiResponseBuilder.buildSuccessResponse(
                "Customer detail by ID",
                customerDetailService.getCustomerDetailById(customerDetailId)
        );
    }

    @GetMapping("/user/{userId}/customer-details")
    public ApiResponse<CustomerDetailDTO> getByUserId(@PathVariable String userId) {
        return ApiResponseBuilder.buildSuccessResponse(
                "Customer detail by user ID",
                customerDetailService.getCustomerDetailByUserId(userId)
        );
    }

    @GetMapping("/customer-details")
    public ApiResponse<List<CustomerDetailDTO>> getAll() {
        return ApiResponseBuilder.buildSuccessResponse(
                "Find all customer details",
                customerDetailService.getAllCustomerDetails()
        );
    }

    @PutMapping("/customer-details/{customerDetailId}")
    public ApiResponse<CustomerDetailDTO> update(@Valid @PathVariable("customerDetailId") String customerDetailId,
                                                 @RequestBody CustomerDetailRequest request) {
        return ApiResponseBuilder.buildSuccessResponse(
                "Update customer detail successful",
                customerDetailService.updateCustomerDetail(customerDetailId, request)
        );
    }

    @DeleteMapping("/customer-details/{customerDetailId}")
    public ApiResponse<Void> delete(@PathVariable("customerDetailId") String customerDetailId) {
        customerDetailService.deleteCustomerDetail(customerDetailId);
        return ApiResponseBuilder.buildSuccessResponse("Delete customer detail successful", null);
    }
}
