package com.capstone.ads.model;

import com.capstone.ads.model.enums.FileTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class FileData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String description;
    String contentType;
    String imageUrl;

    @Enumerated(EnumType.STRING)
    FileTypeEnum fileType;

    Long fileSize;
    
    @CreationTimestamp
    LocalDateTime createdAt;
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne
    CustomDesignRequests customDesignRequests;

    @ManyToOne
    DemoDesigns demoDesigns;

    @ManyToOne
    ProgressLogs progressLogs;
}
