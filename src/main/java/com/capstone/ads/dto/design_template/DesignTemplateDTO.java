package com.capstone.ads.dto.design_template;

import com.capstone.ads.dto.CoreDTO;
import com.capstone.ads.dto.user.UserDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DesignTemplateDTO {
    String id;
    String name;
    String description;
    String image;
    String negativePrompt;
    Integer width;
    Integer height;
    Boolean isAvailable;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    UserDTO users;
    CoreDTO productTypes;
}
