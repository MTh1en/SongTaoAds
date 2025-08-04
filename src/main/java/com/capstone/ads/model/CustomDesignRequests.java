package com.capstone.ads.model;

import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class CustomDesignRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String code;
    String requirements;
    Long totalPrice;
    Long depositAmount;
    Long remainingAmount;
    String finalDesignImage;
    Boolean isNeedSupport;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    CustomDesignRequestStatus status;

    @OneToMany(mappedBy = "customDesignRequests")
    List<DemoDesigns> demoDesigns;

    @OneToMany(mappedBy = "customDesignRequests")
    List<PriceProposal> priceProposals;

    @OneToMany(mappedBy = "customDesignRequests")
    List<FileData> fileData;

    @ManyToOne
    CustomerDetail customerDetail;

    @ManyToOne
    Users assignDesigner;
}
