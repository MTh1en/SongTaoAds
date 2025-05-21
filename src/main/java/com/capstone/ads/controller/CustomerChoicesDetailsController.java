package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customerchoicedetail.CustomerChoicesDetailsDTO;
import com.capstone.ads.service.CustomerChoicesDetailsService;
import com.capstone.ads.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerChoicesDetailsController {
    private final CustomerChoicesDetailsService service;

    @PostMapping("/customer-choices/{customerChoiceId}/attribute-values/{attributeValueId}")
    public ApiResponse<CustomerChoicesDetailsDTO> create(@PathVariable String customerChoiceId, @PathVariable String attributeValueId) {
        return ApiResponseBuilder.buildSuccessResponse("Create customer choices detail successful", service.create(customerChoiceId, attributeValueId));
    }

    @PutMapping("/customer-choices-details/{customerChoiceDetailId}/attribute-values/{attributeValueId}")
    public ApiResponse<CustomerChoicesDetailsDTO> updateAttributeValue(@PathVariable String customerChoiceDetailId,
                                                                       @PathVariable String attributeValueId) {
        return ApiResponseBuilder.buildSuccessResponse("Update customer choices size successful", service.updateAttributeValue(customerChoiceDetailId, attributeValueId));
    }

    @GetMapping("/customer-choice-details/{customerChoiceDetailId}")
    public ApiResponse<CustomerChoicesDetailsDTO> getById(@PathVariable String customerChoiceDetailId) {
        return ApiResponseBuilder.buildSuccessResponse("customer choices detail by Id", service.findById(customerChoiceDetailId));
    }

    @GetMapping("/customer-choices/{customerChoiceId}/customer-choice-details")
    public ApiResponse<List<CustomerChoicesDetailsDTO>> getNewestByCustomer(@PathVariable String customerChoiceId) {
        return ApiResponseBuilder.buildSuccessResponse("Find all customer choices detail by product type", service.findAllByCustomerChoicesId(customerChoiceId));
    }

    @DeleteMapping("/customer-choice-details/{customerChoiceDetailId}")
    public ApiResponse<Void> delete(@PathVariable String customerChoiceDetailId) {
        service.delete(customerChoiceDetailId);
        return ApiResponseBuilder.buildSuccessResponse("Delete customer choices detail successful", null);
    }
}
