package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.price_proposal.PriceProposalCreateRequest;
import com.capstone.ads.dto.price_proposal.PriceProposalDTO;
import com.capstone.ads.dto.price_proposal.PriceProposalOfferPricingRequest;
import com.capstone.ads.dto.price_proposal.PriceProposalUpdatePricingRequest;
import com.capstone.ads.service.PriceProposalService;
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
@Tag(name = "PRICE PROPOSAL")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PriceProposalController {
    PriceProposalService priceProposalService;

    @PostMapping("/custom-design-requests/{customDesignRequestId}/price-proposals")
    @Operation(summary = "Sale báo giá thiết kế")
    public ApiResponse<PriceProposalDTO> createPriceProposal(@PathVariable String customDesignRequestId,
                                                             @Valid @RequestBody PriceProposalCreateRequest request) {
        var response = priceProposalService.createPriceProposal(customDesignRequestId, request);
        return ApiResponseBuilder.buildSuccessResponse("Successfully created price proposal", response);
    }

    @PatchMapping("/price-proposals/{priceProposalId}/pricing")
    @Operation(summary = "Sale cập nhật lại giá đã báo nếu cần")
    public ApiResponse<PriceProposalDTO> updatePricing(@PathVariable String priceProposalId,
                                                       @Valid @RequestBody PriceProposalUpdatePricingRequest request) {
        var response = priceProposalService.updatePricing(priceProposalId, request);
        return ApiResponseBuilder.buildSuccessResponse("Successfully updated price proposal", response);
    }

    @PatchMapping("/price-proposals/{priceProposalId}/offer")
    @Operation(summary = "Khách hàng từ chối thiết kế và offer 1 giá khác")
    public ApiResponse<PriceProposalDTO> offerPricing(@PathVariable String priceProposalId,
                                                      @Valid @RequestBody PriceProposalOfferPricingRequest request) {
        var response = priceProposalService.offerPricing(priceProposalId, request);
        return ApiResponseBuilder.buildSuccessResponse("Successfully offer price proposal", response);
    }

    @PatchMapping("/price-proposals/{priceProposalId}/approve")
    @Operation(summary = "Khách hàng chấp nhận báo giá")
    public ApiResponse<PriceProposalDTO> approvePricing(@PathVariable String priceProposalId) {
        var response = priceProposalService.approvePricing(priceProposalId);
        return ApiResponseBuilder.buildSuccessResponse("Successfully approved price proposal", response);
    }

    @GetMapping("/custom-design-requests/{customDesignRequestId}/price-proposals")
    @Operation(summary = "Xem tất cả lịch sử báo giá theo request")
    public ApiResponse<List<PriceProposalDTO>> getPriceProposals(@PathVariable String customDesignRequestId) {
        var response = priceProposalService.findPriceProposalByCustomerDesignRequestId(customDesignRequestId);
        return ApiResponseBuilder.buildSuccessResponse("Successfully retrieved price proposals", response);
    }
}
