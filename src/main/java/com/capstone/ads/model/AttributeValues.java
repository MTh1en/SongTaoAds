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
public class AttributeValues {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String unit;
    Long materialPrice;
    Long unitPrice;
    Boolean isMultiplier;
    Boolean isAvailable;

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne
    Attributes attributes;

    @OneToMany(mappedBy = "attributeValues")
    List<CustomerChoiceDetails> customerChoiceDetails;

    @OneToMany(mappedBy = "attributeValues")
    List<Backgrounds> backgrounds;
}
