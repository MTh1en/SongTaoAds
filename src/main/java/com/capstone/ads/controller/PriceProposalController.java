package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.price_proposal.PriceProposalCreateRequest;
import com.capstone.ads.dto.price_proposal.PriceProposalDTO;
import com.capstone.ads.dto.price_proposal.PriceProposalOfferPricingRequest;
import com.capstone.ads.dto.price_proposal.PriceProposalUpdatePricingRequest;
import com.capstone.ads.service.PriceProposalService;
import com.capstone.ads.utils.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PriceProposalController {
    private final PriceProposalService priceProposalService;

    @PostMapping("/custom-design-requests/{customDesignRequestId}/price-proposals")
    public ApiResponse<PriceProposalDTO> createPriceProposal(@Valid @PathVariable String customDesignRequestId,
                                                             @RequestBody PriceProposalCreateRequest request) {
        var response = priceProposalService.createPriceProposal(customDesignRequestId, request);
        return ApiResponseBuilder.buildSuccessResponse("Successfully created price proposal", response);
    }

    @PatchMapping("/price-proposals/{priceProposalId}/pricing")
    public ApiResponse<PriceProposalDTO> updatePricing(@Valid @PathVariable String priceProposalId,
                                                       @RequestBody PriceProposalUpdatePricingRequest request) {
        var response = priceProposalService.updatePricing(priceProposalId, request);
        return ApiResponseBuilder.buildSuccessResponse("Successfully updated price proposal", response);
    }

    @PatchMapping("/price-proposals/{priceProposalId}/offer")
    public ApiResponse<PriceProposalDTO> offerPricing(@Valid @PathVariable String priceProposalId,
                                                      @RequestBody PriceProposalOfferPricingRequest request) {
        var response = priceProposalService.offerPricing(priceProposalId, request);
        return ApiResponseBuilder.buildSuccessResponse("Successfully offer price proposal", response);
    }

    @PatchMapping("/price-proposals/{priceProposalId}/approve")
    public ApiResponse<PriceProposalDTO> approvePricing(@Valid @PathVariable String priceProposalId) {
        var response = priceProposalService.approvePricing(priceProposalId);
        return ApiResponseBuilder.buildSuccessResponse("Successfully approved price proposal", response);
    }

    @GetMapping("/custom-design-requests/{customDesignRequestId}/price-proposals")
    public ApiResponse<List<PriceProposalDTO>> getPriceProposals(@Valid @PathVariable String customDesignRequestId) {
        var response = priceProposalService.findPriceProposalByCustomerDesignRequestId(customDesignRequestId);
        return ApiResponseBuilder.buildSuccessResponse("Successfully retrieved price proposals", response);
    }
}
