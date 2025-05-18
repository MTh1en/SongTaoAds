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
public class AIDesigns {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String fileName;
    Long fileSize;
    String imageUrl;
    String contentType;
    Double width;
    Double height;
    Boolean isFinal;

    @ManyToOne
    CustomerDetail customerDetail;
}
