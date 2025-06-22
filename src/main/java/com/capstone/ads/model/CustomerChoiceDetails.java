package com.capstone.ads.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class CustomerChoiceDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    Long subTotal;
    Boolean isMultiplier;
    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne
    @JsonManagedReference
    CustomerChoices customerChoices;

    @ManyToOne
    @JsonManagedReference
    AttributeValues attributeValues;
}
