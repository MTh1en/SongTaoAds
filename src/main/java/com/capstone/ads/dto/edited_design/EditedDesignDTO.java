package com.capstone.ads.dto.edited_design;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditedDesignDTO {
    String id;
    String editedImage;
    String customerNote;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String customerDetail;
    String designTemplates;
}
