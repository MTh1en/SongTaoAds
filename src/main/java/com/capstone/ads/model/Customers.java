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
public class Customers implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @OrderBy
    String id;
    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    String fullName;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false, unique = true)
    String phone;

    @Column(nullable = false)
    String password;

    @Column(unique = true, columnDefinition = "VARCHAR(MAX)")
    String avatar;
    Boolean isActive = false;

    @CreationTimestamp
    Timestamp createAt;
    @UpdateTimestamp
    Timestamp updateAt;

    @Column(nullable = false, unique = true)
    String logoUrl;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    String companyName;

    @Column(columnDefinition = "NVARCHAR(255)")
    String tagLine;

    @Column(columnDefinition = "NVARCHAR(255)")
    String contactInfo;
}
