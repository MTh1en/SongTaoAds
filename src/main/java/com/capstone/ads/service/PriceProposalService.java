package com.capstone.ads.service;

import com.capstone.ads.dto.price_proposal.PriceProposalCreateRequest;
import com.capstone.ads.dto.price_proposal.PriceProposalDTO;
import com.capstone.ads.dto.price_proposal.PriceProposalOfferPricingRequest;
import com.capstone.ads.dto.price_proposal.PriceProposalUpdatePricingRequest;
import com.capstone.ads.model.PriceProposal;

import java.util.List;

public interface PriceProposalService {
    PriceProposalDTO createPriceProposal(String customerDesignRequestId, PriceProposalCreateRequest request);

    PriceProposalDTO updatePricing(String priceProposalId, PriceProposalUpdatePricingRequest request);

    PriceProposalDTO offerPricing(String priceProposalId, PriceProposalOfferPricingRequest request);

    PriceProposalDTO approvePricing(String priceProposalId);

    List<PriceProposalDTO> findPriceProposalByCustomerDesignRequestId(String customerDesignRequestId);

    //INTERNAL FUNCTION
    PriceProposal getPriceProposalById(String priceProposalId);
}
