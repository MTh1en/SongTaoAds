package com.capstone.ads.model;

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
public class CustomerChoices {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    Long totalAmount;

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne
    ProductTypes productTypes;

    @OneToOne
    Users users;

    @OneToMany(
            mappedBy = "customerChoices",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    List<CustomerChoiceDetails> customerChoiceDetails;

    @OneToMany(
            mappedBy = "customerChoices",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    List<CustomerChoiceSizes> customerChoiceSizes;

    @OneToMany(
            mappedBy = "customerChoices",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    List<CustomerChoiceCosts> customerChoiceCosts;
}
