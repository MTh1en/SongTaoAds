package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customer_choice_detail.CustomerChoicesDetailsDTO;
import com.capstone.ads.service.CustomerChoiceDetailsService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerChoiceDetailsController {
    private final CustomerChoiceDetailsService service;

    @PostMapping("/customer-choices/{customerChoiceId}/attribute-values/{attributeValueId}")
    public ApiResponse<CustomerChoicesDetailsDTO> createCustomerChoiceDetail(@PathVariable String customerChoiceId,
                                                                             @PathVariable String attributeValueId) {
        var response = service.createCustomerChoiceDetail(customerChoiceId, attributeValueId);
        return ApiResponseBuilder.buildSuccessResponse("Create customer choices detail successful", response);
    }

    @PutMapping("/customer-choice-details/{customerChoiceDetailId}/attribute-values/{attributeValueId}")
    public ApiResponse<CustomerChoicesDetailsDTO> updateAttributeValueInCustomerChoiceDetail(
            @PathVariable String customerChoiceDetailId,
            @PathVariable String attributeValueId) {
        var response = service.updateAttributeValueInCustomerChoiceDetail(customerChoiceDetailId, attributeValueId);
        return ApiResponseBuilder.buildSuccessResponse("Update customer choices size successful", response);
    }

    @GetMapping("/customer-choice-details/{customerChoiceDetailId}")
    public ApiResponse<CustomerChoicesDetailsDTO> findCustomerChoiceDetailById(@PathVariable String customerChoiceDetailId) {
        var response = service.findCustomerChoiceDetailById(customerChoiceDetailId);
        return ApiResponseBuilder.buildSuccessResponse("customer choices detail by Id", response);
    }

    @GetMapping("/customer-choices/{customerChoiceId}/customer-choice-details")
    public ApiResponse<List<CustomerChoicesDetailsDTO>> findAllCustomerChoiceDetailByCustomerChoicesId(@PathVariable String customerChoiceId) {
        var response = service.findAllCustomerChoiceDetailByCustomerChoicesId(customerChoiceId);
        return ApiResponseBuilder.buildSuccessResponse("Find all customer choices detail by product type", response);
    }

    @DeleteMapping("/customer-choice-details/{customerChoiceDetailId}")
    public ApiResponse<Void> hardDeleteCustomerChoiceDetail(@PathVariable String customerChoiceDetailId) {
        service.hardDeleteCustomerChoiceDetail(customerChoiceDetailId);
        return ApiResponseBuilder.buildSuccessResponse("Delete customer choices detail successful", null);
    }
}
