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
public class CustomerChoiceCosts {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    Long value;

    @ManyToOne
    CustomerChoices customerChoices;

    @ManyToOne
    CostTypes costTypes;
}
