package com.capstone.ads.model;

import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.model.json.CustomerChoiceHistories;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @CreationTimestamp
    LocalDateTime orderDate;

    Timestamp deliveryDate;
    String address;
    Double totalAmount;
    Double depositAmount;
    Double remainingAmount;
    String note;

    @UpdateTimestamp
    LocalDateTime updateDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    CustomerChoiceHistories customerChoiceHistories;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    @ManyToOne
    Users users;

    @OneToMany(mappedBy = "orders")
    List<Payments> payments;

    @OneToOne
    AIDesigns aiDesigns;

    @OneToOne
    CustomDesigns customDesigns;
}
