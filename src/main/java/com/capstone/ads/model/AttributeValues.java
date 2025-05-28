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
    Double materialPrice;
    Double unitPrice;
    Boolean isAvailable;

    @CreationTimestamp
    LocalDateTime createAt;
    @UpdateTimestamp
    LocalDateTime updateAt;

    @ManyToOne
    Attributes attributes;

    @OneToMany(mappedBy = "attributeValues")
    List<CustomerChoiceDetails> customerChoiceDetails;
}
