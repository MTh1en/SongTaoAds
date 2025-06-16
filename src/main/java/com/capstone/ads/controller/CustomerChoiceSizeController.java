package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeCreateRequest;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeDTO;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeUpdateRequest;
import com.capstone.ads.service.CustomerChoiceSizesService;
import com.capstone.ads.utils.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerChoiceSizeController {
    private final CustomerChoiceSizesService service;

    @PostMapping("/customer-choices/{customerChoicesId}/sizes/{sizeId}")
    public ApiResponse<CustomerChoicesSizeDTO> createCustomerChoiceSize(@Valid @PathVariable String customerChoicesId,
                                                                        @PathVariable String sizeId,
                                                                        @RequestBody CustomerChoicesSizeCreateRequest request) {
        var response = service.createCustomerChoiceSize(customerChoicesId, sizeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Create customer choices size successful", response);
    }

    @PutMapping("/customer-choice-sizes/{customerChoiceSizeId}")
    public ApiResponse<CustomerChoicesSizeDTO> updateValueInCustomerChoiceSize(@Valid @PathVariable String customerChoiceSizeId,
                                                                               @RequestBody CustomerChoicesSizeUpdateRequest request) {
        var response = service.updateValueInCustomerChoiceSize(customerChoiceSizeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update customer choices size successful", response);
    }

    @GetMapping("/customer-choice-sizes/{customerChoiceSizeId}")
    public ApiResponse<CustomerChoicesSizeDTO> findCustomerChoiceSizeById(@PathVariable String customerChoiceSizeId) {
        var response = service.findCustomerChoiceSizeById(customerChoiceSizeId);
        return ApiResponseBuilder.buildSuccessResponse("customer choices size by Id", response);
    }

    @GetMapping("/customer-choices/{customerChoicesId}/customer-choices-value")
    public ApiResponse<List<CustomerChoicesSizeDTO>> findAllCustomerChoiceSizeByCustomerChoicesId(@PathVariable String customerChoicesId) {
        var response = service.findAllCustomerChoiceSizeByCustomerChoicesId(customerChoicesId);
        return ApiResponseBuilder.buildSuccessResponse("Find all customer choices size by customer choices", response);
    }

    @DeleteMapping("/customer-choice-sizes/{customerChoiceSizeId}")
    public ApiResponse<Void> hardDeleteCustomerChoiceSize(@PathVariable String customerChoiceSizeId) {
        service.hardDeleteCustomerChoiceSize(customerChoiceSizeId);
        return ApiResponseBuilder.buildSuccessResponse("Delete customer choices size successful", null);
    }
}
