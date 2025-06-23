package com.capstone.ads.repository.internal;

import com.capstone.ads.model.PriceProposal;
import com.capstone.ads.model.enums.PriceProposalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceProposalRepository extends JpaRepository<PriceProposal, String> {
    List<PriceProposal> findByCustomDesignRequests_IdOrderByCreateAtDescUpdateAtDesc(String id);

    boolean existsByCustomDesignRequests_IdAndStatus(String id, PriceProposalStatus status);
}