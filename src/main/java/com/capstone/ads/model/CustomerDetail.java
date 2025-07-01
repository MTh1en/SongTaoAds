package com.capstone.ads.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class CustomerDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, unique = true)
    String logoUrl;

    @Column(nullable = false)
    String companyName;
    String address;
    String contactInfo;

    @OneToOne
    Users users;

    @OneToMany(mappedBy = "customerDetail")
    List<EditedDesigns> editedDesigns;

    @OneToMany(mappedBy = "customerDetail")
    List<CustomDesignRequests> customDesignRequests;
}
