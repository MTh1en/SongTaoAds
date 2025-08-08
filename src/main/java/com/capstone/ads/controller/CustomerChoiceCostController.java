package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.customer_choice_cost.CustomerChoiceCostDTO;
import com.capstone.ads.service.CustomerChoiceCostsService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "CUSTOMER CHOICE COST")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerChoiceCostController {
    CustomerChoiceCostsService customerChoiceCostsService;

    @GetMapping("/customer-choices/{customerChoiceId}/customer-choice-costs")
    @Operation(summary = "Xem các chi phí theo lựa chọn của khách hàng")
    public ApiResponse<List<CustomerChoiceCostDTO>> findCustomerChoiceCostByCustomerChoice(@PathVariable String customerChoiceId) {
        var response = customerChoiceCostsService.findCustomerChoiceCostByCustomerChoice(customerChoiceId);
        return ApiResponseBuilder.buildSuccessResponse("Xem tất cả các chi phí theo lựa chọn khách hàng thành công", response);
    }
}
