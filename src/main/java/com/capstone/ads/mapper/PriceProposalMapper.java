package com.capstone.ads.mapper;

import com.capstone.ads.dto.price_proposal.PriceProposalCreateRequest;
import com.capstone.ads.dto.price_proposal.PriceProposalDTO;
import com.capstone.ads.dto.price_proposal.PriceProposalOfferPricingRequest;
import com.capstone.ads.dto.price_proposal.PriceProposalUpdatePricingRequest;
import com.capstone.ads.model.PriceProposal;
import com.capstone.ads.model.enums.PriceProposalStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PriceProposalMapper {
    PriceProposalDTO toDTO(PriceProposal priceProposal);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(initStatus())")
    PriceProposal mapCreateRequestToEntity(PriceProposalCreateRequest createRequest);

    void mapUpdatePricingRequestToEntity(PriceProposalUpdatePricingRequest request, @MappingTarget PriceProposal priceProposal);

    void mapOfferPricingRequestToEntity(PriceProposalOfferPricingRequest request, @MappingTarget PriceProposal priceProposal);

    default PriceProposalStatus initStatus() {
        return PriceProposalStatus.PENDING;
    }
}
