package com.capstone.ads.model;

import com.capstone.ads.model.json.CustomerChoiceHistories;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    Long detailConstructionAmount;
    Long quantity;
    Long detailDesignAmount;
    Long detailDepositDesignAmount;
    Long detailRemainingDesignAmount;


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    CustomerChoiceHistories customerChoiceHistories;

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @OneToOne
    EditedDesigns editedDesigns;

    @OneToOne
    CustomDesignRequests customDesignRequests;

    @ManyToOne
    Orders orders;
}
