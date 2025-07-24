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
public class Contractors {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String address;
    String phone;
    String email;
    Boolean isInternal;
    Boolean isAvailable;

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "contractors")
    List<Orders> orders;
}
