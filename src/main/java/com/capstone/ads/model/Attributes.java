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
public class Attributes {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String calculateFormula;
    Boolean isAvailable;
    Boolean isCore;

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne
    ProductTypes productTypes;

    @OneToMany(mappedBy = "attributes")
    List<AttributeValues> attributeValues;
}
