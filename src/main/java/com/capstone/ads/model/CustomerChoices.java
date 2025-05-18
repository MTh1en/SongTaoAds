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

    Double totalAmount;

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne
    Users users;
    @ManyToOne
    ProductType productType;
    @OneToMany(mappedBy = "customerChoices")
    List<CustomerChoicesDetails> customerChoicesDetails;

    @OneToMany(mappedBy = "customerChoices")
    List<CustomerChoicesSize> customerChoicesSizes;
}
