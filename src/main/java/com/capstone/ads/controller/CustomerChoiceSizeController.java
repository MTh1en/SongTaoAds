package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeCreateRequest;
import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeDTO;
import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeUpdateRequest;
import com.capstone.ads.service.CustomerChoiceSizesService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "CUSTOMER CHOICE SIZE")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerChoiceSizeController {
    CustomerChoiceSizesService service;

    @PostMapping("/customer-choices/{customerChoicesId}/sizes/{sizeId}")
    @Operation(summary = "Nhập kích thước cho đơn hàng")
    public ApiResponse<CustomerChoicesSizeDTO> createCustomerChoiceSize(
            @PathVariable String customerChoicesId,
            @PathVariable String sizeId,
            @Valid @RequestBody CustomerChoicesSizeCreateRequest request) {
        var response = service.createCustomerChoiceSize(customerChoicesId, sizeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Create customer choices size successful", response);
    }

    @PutMapping("/customer-choice-sizes/{customerChoiceSizeId}")
    @Operation(summary = "Cập nhật lại kích thước đã chọn")
    public ApiResponse<CustomerChoicesSizeDTO> updateValueInCustomerChoiceSize(
            @PathVariable String customerChoiceSizeId,
            @Valid @RequestBody CustomerChoicesSizeUpdateRequest request) {
        var response = service.updateValueInCustomerChoiceSize(customerChoiceSizeId, request);
        return ApiResponseBuilder.buildSuccessResponse("Update customer choices size successful", response);
    }

    @GetMapping("/customer-choice-sizes/{customerChoiceSizeId}")
    @Operation(summary = "Xem kích thước đã chọn theo ID")
    public ApiResponse<CustomerChoicesSizeDTO> findCustomerChoiceSizeById(@PathVariable String customerChoiceSizeId) {
        var response = service.findCustomerChoiceSizeById(customerChoiceSizeId);
        return ApiResponseBuilder.buildSuccessResponse("customer choices size by Id", response);
    }

    @GetMapping("/customer-choices/{customerChoicesId}/customer-choices-value")
    @Operation(summary = "Xem kích thước theo loại sản phẩm đã chọn")
    public ApiResponse<List<CustomerChoicesSizeDTO>> findAllCustomerChoiceSizeByCustomerChoicesId(@PathVariable String customerChoicesId) {
        var response = service.findAllCustomerChoiceSizeByCustomerChoicesId(customerChoicesId);
        return ApiResponseBuilder.buildSuccessResponse("Find all customer choices size by customer choices", response);
    }

    @GetMapping("/customer-choice-sizes/{customerChoiceSizeId}/pixel-value")
    @Operation(summary = "Chuyển kích thước khách hàng chọn sang giá trị pixel")
    public ApiResponse<Long> convertToPixelValue(@PathVariable String customerChoiceSizeId) {
        var response = service.convertToPixel(customerChoiceSizeId);
        return ApiResponseBuilder.buildSuccessResponse("Convert pixel value successful", response);
    }

    @DeleteMapping("/customer-choice-sizes/{customerChoiceSizeId}")
    @Operation(summary = "Xóa cứng kích thước đã chọn")
    public ApiResponse<Void> hardDeleteCustomerChoiceSize(@PathVariable String customerChoiceSizeId) {
        service.hardDeleteCustomerChoiceSize(customerChoiceSizeId);
        return ApiResponseBuilder.buildSuccessResponse("Delete customer choices size successful", null);
    }
}
