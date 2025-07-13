package com.capstone.ads.model;

import com.capstone.ads.model.enums.CustomDesignRequestStatus;
import com.capstone.ads.model.json.CustomerChoiceHistories;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

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
    String requirements;
    Long totalPrice;
    Long depositAmount;
    Long remainingAmount;
    String finalDesignImage;
    Boolean isNeedSupport;
    Boolean hasOrder;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    CustomerChoiceHistories customerChoiceHistories;

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
    List<Payments> payments;

    @OneToMany(mappedBy = "customDesignRequests")
    List<FileData> fileData;

    @ManyToOne
    CustomerDetail customerDetail;

    @ManyToOne
    Users assignDesigner;
}
