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
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String calculateFormula;
    Boolean isAvailable;

    @CreationTimestamp
    LocalDateTime createAt;
    @UpdateTimestamp
    LocalDateTime updateAt;

    @OneToMany(mappedBy = "productType")
    List<Attributes> attributes;

    @OneToMany(mappedBy = "productType")
    List<CustomerChoices> customerChoices;

    @OneToMany(mappedBy = "productType")
    List<ProductTypeSize> productTypeSizes;
}
