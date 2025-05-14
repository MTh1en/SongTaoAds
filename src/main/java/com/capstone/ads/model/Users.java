package com.capstone.ads.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Users {
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
    Boolean isActive;

    @CreationTimestamp
    Timestamp createAt;
    @UpdateTimestamp
    Timestamp updateAt;

    @ManyToOne
    private Role role;
}
