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
public class EditedDesigns {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String editedImage;
    String customerNote;

    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne
    CustomerDetail customerDetail;

    @ManyToOne
    DesignTemplates designTemplates;

    @ManyToOne
    Backgrounds backgrounds;
}
