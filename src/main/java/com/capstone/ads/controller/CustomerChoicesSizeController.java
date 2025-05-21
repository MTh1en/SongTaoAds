package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeCreateRequest;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeDTO;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeUpdateRequest;
import com.capstone.ads.service.CustomerChoicesSizeService;
import com.capstone.ads.utils.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerChoicesSizeController {
    private final CustomerChoicesSizeService service;

    @PostMapping("/customer-choices/{customerChoicesId}/sizes/{sizeId}")
    public ApiResponse<CustomerChoicesSizeDTO> create(@Valid @PathVariable String customerChoicesId,
                                                      @PathVariable String sizeId,
                                                      @RequestBody CustomerChoicesSizeCreateRequest request) {
        return ApiResponseBuilder.buildSuccessResponse("Create customer choices size successful", service.create(customerChoicesId, sizeId, request));
    }

    @PutMapping("/customer-choices-sizes/{customerChoiceSizeId}")
    public ApiResponse<CustomerChoicesSizeDTO> update(@Valid @PathVariable String customerChoiceSizeId,
                                                      @RequestBody CustomerChoicesSizeUpdateRequest request) {
        return ApiResponseBuilder.buildSuccessResponse("Update customer choices size successful", service.update(customerChoiceSizeId, request));
    }

    @GetMapping("/customer-choice-values/{customerChoiceSizeId}")
    public ApiResponse<CustomerChoicesSizeDTO> getById(@PathVariable String customerChoiceSizeId) {
        return ApiResponseBuilder.buildSuccessResponse("customer choices size by Id", service.findById(customerChoiceSizeId));
    }

    @GetMapping("/customer-choices/{customerChoicesId}/customer-choices-value")
    public ApiResponse<List<CustomerChoicesSizeDTO>> getAllByCustomerChoices(@PathVariable String customerChoicesId) {
        return ApiResponseBuilder.buildSuccessResponse("Find all customer choices size by customer choices", service.findAllByCustomerChoicesId(customerChoicesId));
    }

    @DeleteMapping("/customer-choice-values/{customerChoiceSizeId}")
    public ApiResponse<Void> delete(@PathVariable String customerChoiceSizeId) {
        service.delete(customerChoiceSizeId);
        return ApiResponseBuilder.buildSuccessResponse("Delete customer choices size successful", null);
    }
}
