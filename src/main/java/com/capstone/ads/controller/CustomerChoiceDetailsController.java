package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customer_choice_detail.CustomerChoicesDetailsDTO;
import com.capstone.ads.service.CustomerChoiceDetailsService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "CUSTOMER CHOICE DETAIL")
public class CustomerChoiceDetailsController {
    private final CustomerChoiceDetailsService service;

    @PostMapping("/customer-choices/{customerChoiceId}/attribute-values/{attributeValueId}")
    @Operation(summary = "Chọn giá trị thuộc tính")
    public ApiResponse<CustomerChoicesDetailsDTO> createCustomerChoiceDetail(@PathVariable String customerChoiceId,
                                                                             @PathVariable String attributeValueId) {
        var response = service.createCustomerChoiceDetail(customerChoiceId, attributeValueId);
        return ApiResponseBuilder.buildSuccessResponse("Create customer choices detail successful", response);
    }

    @PutMapping("/customer-choice-details/{customerChoiceDetailId}/attribute-values/{attributeValueId}")
    @Operation(summary = "Cập nhật giá trị thuộc tính khác")
    public ApiResponse<CustomerChoicesDetailsDTO> updateAttributeValueInCustomerChoiceDetail(
            @PathVariable String customerChoiceDetailId,
            @PathVariable String attributeValueId) {
        var response = service.updateAttributeValueInCustomerChoiceDetail(customerChoiceDetailId, attributeValueId);
        return ApiResponseBuilder.buildSuccessResponse("Update customer choices size successful", response);
    }

    @GetMapping("/customer-choice-details/{customerChoiceDetailId}")
    @Operation(summary = "Xem giá trị thuộc tính đã chọn theo ID")
    public ApiResponse<CustomerChoicesDetailsDTO> findCustomerChoiceDetailById(@PathVariable String customerChoiceDetailId) {
        var response = service.findCustomerChoiceDetailById(customerChoiceDetailId);
        return ApiResponseBuilder.buildSuccessResponse("customer choices detail by Id", response);
    }

    @GetMapping("/customer-choices/{customerChoiceId}/customer-choice-details")
    @Operation(summary = "Xem thuộc tính theo lựa chọn loại sản phẩm đã chọn")
    public ApiResponse<List<CustomerChoicesDetailsDTO>> findAllCustomerChoiceDetailByCustomerChoicesId(@PathVariable String customerChoiceId) {
        var response = service.findAllCustomerChoiceDetailByCustomerChoicesId(customerChoiceId);
        return ApiResponseBuilder.buildSuccessResponse("Find all customer choices detail by product type", response);
    }

    @DeleteMapping("/customer-choice-details/{customerChoiceDetailId}")
    @Operation(summary = "Xóa cứng giá trị thuộc tính đã chọn")
    public ApiResponse<Void> hardDeleteCustomerChoiceDetail(@PathVariable String customerChoiceDetailId) {
        service.hardDeleteCustomerChoiceDetail(customerChoiceDetailId);
        return ApiResponseBuilder.buildSuccessResponse("Delete customer choices detail successful", null);
    }
}
