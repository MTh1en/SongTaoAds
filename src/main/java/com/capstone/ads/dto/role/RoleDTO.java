package com.capstone.ads.dto.role;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDTO {
    String name;
    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
