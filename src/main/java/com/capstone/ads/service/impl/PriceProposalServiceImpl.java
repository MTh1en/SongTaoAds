package com.capstone.ads.service.impl;

import com.capstone.ads.dto.price_proposal.PriceProposalCreateRequest;
import com.capstone.ads.dto.price_proposal.PriceProposalDTO;
import com.capstone.ads.dto.price_proposal.PriceProposalOfferPricingRequest;
import com.capstone.ads.dto.price_proposal.PriceProposalUpdatePricingRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.PriceProposalMapper;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.PriceProposal;
import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import com.capstone.ads.model.enums.PriceProposalStatus;
import com.capstone.ads.repository.internal.PriceProposalRepository;
import com.capstone.ads.service.CustomDesignRequestService;
import com.capstone.ads.service.PriceProposalService;
import com.capstone.ads.utils.PriceProposalStateValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceProposalServiceImpl implements PriceProposalService {
    private final CustomDesignRequestService customDesignRequestService;
    private final PriceProposalRepository priceProposalRepository;
    private final PriceProposalMapper priceProposalMapper;
    private final PriceProposalStateValidator priceProposalStateValidator;

    @Override
    @Transactional
    public PriceProposalDTO createPriceProposal(String customerDesignRequestId, PriceProposalCreateRequest request) {
        CustomDesignRequests customDesignRequests = customDesignRequestService.getCustomDesignRequestById(customerDesignRequestId);

        validateCreatedDuplicatedPriceProposal(customerDesignRequestId);
        PriceProposal priceProposal = priceProposalMapper.mapCreateRequestToEntity(request);
        priceProposal.setCustomDesignRequests(customDesignRequests);
        priceProposal = priceProposalRepository.save(priceProposal);

        customDesignRequestService.updateCustomDesignRequestStatus(customerDesignRequestId, CustomDesignRequestStatus.PRICING_NOTIFIED);

        return priceProposalMapper.toDTO(priceProposal);
    }

    @Override
    @Transactional
    public PriceProposalDTO updatePricing(String priceProposalId, PriceProposalUpdatePricingRequest request) {
        PriceProposal priceProposal = getPriceProposalById(priceProposalId);
        priceProposalStateValidator.validateTransition(priceProposal.getStatus(), PriceProposalStatus.PENDING);

        priceProposalMapper.mapUpdatePricingRequestToEntity(request, priceProposal);

        priceProposal = priceProposalRepository.save(priceProposal);
        return priceProposalMapper.toDTO(priceProposal);
    }

    @Override
    @Transactional
    public PriceProposalDTO offerPricing(String priceProposalId, PriceProposalOfferPricingRequest request) {
        PriceProposal priceProposal = getPriceProposalById(priceProposalId);
        String customerDesignRequestId = priceProposal.getCustomDesignRequests().getId();
        priceProposalStateValidator.validateTransition(priceProposal.getStatus(), PriceProposalStatus.REJECTED);

        priceProposalMapper.mapOfferPricingRequestToEntity(request, priceProposal);
        priceProposal.setStatus(PriceProposalStatus.REJECTED);
        priceProposal = priceProposalRepository.save(priceProposal);

        customDesignRequestService.updateCustomDesignRequestStatus(customerDesignRequestId, CustomDesignRequestStatus.REJECTED_PRICING);

        return priceProposalMapper.toDTO(priceProposal);
    }

    @Override
    @Transactional
    public PriceProposalDTO approvePricing(String priceProposalId) {
        PriceProposal priceProposal = getPriceProposalById(priceProposalId);
        String customerDesignRequestId = priceProposal.getCustomDesignRequests().getId();
        priceProposalStateValidator.validateTransition(priceProposal.getStatus(), PriceProposalStatus.APPROVED);

        priceProposal.setStatus(PriceProposalStatus.APPROVED);
        priceProposal = priceProposalRepository.save(priceProposal);

        customDesignRequestService.updateCustomDesignRequestApprovedPricing(customerDesignRequestId, priceProposal.getTotalPrice(), priceProposal.getDepositAmount());

        return priceProposalMapper.toDTO(priceProposal);
    }

    @Override
    public List<PriceProposalDTO> findPriceProposalByCustomerDesignRequestId(String customerDesignRequestId) {
        return priceProposalRepository.findByCustomDesignRequests_IdOrderByCreatedAtDescUpdatedAtDesc(customerDesignRequestId).stream()
                .map(priceProposalMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PriceProposal getPriceProposalById(String priceProposalId) {
        return priceProposalRepository.findById(priceProposalId)
                .orElseThrow(() -> new AppException(ErrorCode.PRICE_PROPOSAL_NOT_FOUND));
    }


    private void validateCreatedDuplicatedPriceProposal(String priceProposalId) {
        if (priceProposalRepository.existsByCustomDesignRequests_IdAndStatus(priceProposalId, PriceProposalStatus.PENDING)) {
            throw new AppException(ErrorCode.PRICE_PROPOSAL_NOT_FOUND);
        }
    }
}
