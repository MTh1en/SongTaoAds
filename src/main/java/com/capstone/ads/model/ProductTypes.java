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
public class ProductTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String image;
    String calculateFormula;
    Boolean isAiGenerated;
    Boolean isAvailable;

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "productTypes")
    List<Attributes> attributes;

    @OneToMany(mappedBy = "productTypes")
    List<CustomerChoices> customerChoices;

    @OneToMany(mappedBy = "productTypes")
    List<ProductTypeSizes> productTypeSizes;

    @OneToMany(mappedBy = "productTypes")
    List<DesignTemplates> designTemplates;

    @OneToMany(mappedBy = "productTypes")
    List<CostTypes> costTypes;
}
