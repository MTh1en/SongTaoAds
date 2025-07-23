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
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @OrderBy
    String id;
    @Column(nullable = false)
    String fullName;

    @Column(nullable = false, unique = true)
    String email;

    @Column(unique = true)
    String phone;
    String address;
    String password;

    @Column(unique = true, length = 512)
    String avatar;
    Boolean isActive;

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne
    Roles roles;

    @OneToMany(mappedBy = "users")
    List<Orders> orders;

    @OneToMany(mappedBy = "users")
    List<DesignTemplates> designTemplates;

    @OneToMany(mappedBy = "assignDesigner")
    List<CustomDesignRequests> customDesignRequests;

    @OneToMany(mappedBy = "users")
    List<ChatBotLog> chatBotLogs;

    @OneToMany(mappedBy = "sendBy")
    List<Feedbacks> feedbacks;
}
