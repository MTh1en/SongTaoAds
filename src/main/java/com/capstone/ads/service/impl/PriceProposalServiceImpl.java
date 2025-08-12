package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.NotificationMessage;
import com.capstone.ads.constaint.PredefinedRole;
import com.capstone.ads.dto.price_proposal.PriceProposalCreateRequest;
import com.capstone.ads.dto.price_proposal.PriceProposalDTO;
import com.capstone.ads.dto.price_proposal.PriceProposalOfferPricingRequest;
import com.capstone.ads.dto.price_proposal.PriceProposalUpdatePricingRequest;
import com.capstone.ads.event.CustomDesignRequestChangeStatusEvent;
import com.capstone.ads.event.PriceProposalApprovedEvent;
import com.capstone.ads.event.RoleNotificationEvent;
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
import com.capstone.ads.validator.CustomDesignRequestStateValidator;
import com.capstone.ads.validator.PriceProposalStateValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PriceProposalServiceImpl implements PriceProposalService {
    CustomDesignRequestService customDesignRequestService;
    PriceProposalRepository priceProposalRepository;
    PriceProposalMapper priceProposalMapper;
    PriceProposalStateValidator priceProposalStateValidator;
    CustomDesignRequestStateValidator customDesignRequestStateValidator;
    ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public PriceProposalDTO createPriceProposal(String customerDesignRequestId, PriceProposalCreateRequest request) {
        CustomDesignRequests customDesignRequests = customDesignRequestService.getCustomDesignRequestById(customerDesignRequestId);

        validateCreatedDuplicatedPriceProposal(customerDesignRequestId);
        PriceProposal priceProposal = priceProposalMapper.mapCreateRequestToEntity(request);
        priceProposal.setCustomDesignRequests(customDesignRequests);
        priceProposal = priceProposalRepository.save(priceProposal);

        customDesignRequestStateValidator.validateTransition(
                customDesignRequests.getStatus(),
                CustomDesignRequestStatus.PRICING_NOTIFIED
        );

        eventPublisher.publishEvent(new CustomDesignRequestChangeStatusEvent(
                this,
                customerDesignRequestId,
                CustomDesignRequestStatus.PRICING_NOTIFIED
        ));

        return priceProposalMapper.toDTO(priceProposal);
    }

    @Override
    @Transactional
    public PriceProposalDTO updatePricing(String priceProposalId, PriceProposalUpdatePricingRequest request) {
        PriceProposal priceProposal = getPriceProposalById(priceProposalId);

        priceProposalMapper.mapUpdatePricingRequestToEntity(request, priceProposal);

        priceProposal = priceProposalRepository.save(priceProposal);
        return priceProposalMapper.toDTO(priceProposal);
    }

    @Override
    @Transactional
    public PriceProposalDTO offerPricing(String priceProposalId, PriceProposalOfferPricingRequest request) {
        PriceProposal priceProposal = getPriceProposalById(priceProposalId);
        CustomDesignRequests customerDesignRequest = priceProposal.getCustomDesignRequests();
        priceProposalStateValidator.validateTransition(priceProposal.getStatus(), PriceProposalStatus.REJECTED);

        priceProposalMapper.mapOfferPricingRequestToEntity(request, priceProposal);
        priceProposal.setStatus(PriceProposalStatus.REJECTED);
        priceProposal = priceProposalRepository.save(priceProposal);

        customDesignRequestStateValidator.validateTransition(
                priceProposal.getCustomDesignRequests().getStatus(),
                CustomDesignRequestStatus.REJECTED_PRICING
        );

        eventPublisher.publishEvent(new CustomDesignRequestChangeStatusEvent(
                this,
                customerDesignRequest.getId(),
                CustomDesignRequestStatus.REJECTED_PRICING
        ));

        eventPublisher.publishEvent(new RoleNotificationEvent(
                this,
                PredefinedRole.SALE_ROLE,
                String.format(NotificationMessage.PRICING_REJECTED, customerDesignRequest.getCode())
        ));

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

        customDesignRequestStateValidator.validateTransition(
                priceProposal.getCustomDesignRequests().getStatus(),
                CustomDesignRequestStatus.APPROVED_PRICING
        );

        eventPublisher.publishEvent(new PriceProposalApprovedEvent(
                this,
                customerDesignRequestId,
                priceProposal.getTotalPrice(),
                priceProposal.getDepositAmount(),
                priceProposal.getTotalPrice() - priceProposal.getDepositAmount()
        ));
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
