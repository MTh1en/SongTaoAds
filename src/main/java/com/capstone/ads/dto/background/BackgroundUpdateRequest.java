package com.capstone.ads.dto.background;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BackgroundUpdateRequest {
    String name;
    String description;
    Boolean isAvailable;
}
