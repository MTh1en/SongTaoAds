package com.capstone.ads.model;

import com.capstone.ads.model.enums.AspectRatio;
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
public class DesignTemplates {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;
    String description;
    String image;
    Boolean isAvailable;

    @Enumerated(EnumType.STRING)
    AspectRatio aspectRatio;

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne
    Users users;

    @ManyToOne
    ProductTypes productTypes;

    @OneToMany(mappedBy = "designTemplates")
    List<EditedDesigns> editedDesigns;
}
