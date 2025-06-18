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

    Integer totalPrice;
    Integer depositAmount;
    String rejectionReason;
    Integer totalPriceOffer;
    Integer depositAmountOffer;

    @Enumerated(EnumType.STRING)
    PriceProposalStatus status;

    @CreationTimestamp
    LocalDateTime createAt;
    @UpdateTimestamp
    LocalDateTime updateAt;

    @ManyToOne
    CustomDesignRequests customDesignRequests;
}
