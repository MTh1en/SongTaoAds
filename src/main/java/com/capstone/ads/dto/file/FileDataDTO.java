package com.capstone.ads.dto.file;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileDataDTO {
    String id;
    String name;
    String description;
    String imageUrl;
    String contentType;
    String fileType;
    Long fileSize;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
