package com.capstone.ads.model;

import com.capstone.ads.model.enums.DimensionType;
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
public class ProductTypeSizes {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    Float maxValue;
    Float minValue;

    @Enumerated(EnumType.STRING)
    DimensionType dimensionType;

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne
    ProductTypes productTypes;

    @ManyToOne
    Sizes sizes;
}
