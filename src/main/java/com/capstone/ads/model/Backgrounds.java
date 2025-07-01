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
public class Backgrounds {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String description;
    String backgroundUrl;
    Boolean isAvailable;

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne
    AttributeValues attributeValues;

    @OneToMany(mappedBy = "backgrounds")
    List<EditedDesigns> editedDesigns;
}
