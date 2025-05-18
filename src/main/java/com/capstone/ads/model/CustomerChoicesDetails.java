package com.capstone.ads.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class CustomerChoicesDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    Double subTotal;

    @ManyToOne
    CustomerChoices customerChoices;

    @ManyToOne
    AttributeValues attributeValues;
}
