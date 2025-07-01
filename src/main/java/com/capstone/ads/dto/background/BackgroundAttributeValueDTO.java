package com.capstone.ads.dto.background;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BackgroundAttributeValueDTO {
    String id;
    String name;
}
