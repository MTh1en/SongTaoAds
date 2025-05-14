package com.capstone.ads.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;

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

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    String companyName;

    @Column(columnDefinition = "NVARCHAR(255)")
    String tagLine;

    @Column(columnDefinition = "NVARCHAR(255)")
    String contactInfo;

    @OneToOne(fetch = FetchType.LAZY)
    Users users;
}
