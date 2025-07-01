package com.capstone.ads.model;

import com.capstone.ads.model.enums.PriceProposalStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class PriceProposal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    Long totalPrice;
    Long depositAmount;
    String rejectionReason;
    Long totalPriceOffer;
    Long depositAmountOffer;

    @Enumerated(EnumType.STRING)
    PriceProposalStatus status;

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne
    CustomDesignRequests customDesignRequests;
}
