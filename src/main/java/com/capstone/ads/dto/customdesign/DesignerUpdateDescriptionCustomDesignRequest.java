package com.capstone.ads.dto.customdesign;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DesignerUpdateDescriptionCustomDesignRequest {
    String designerDescription;
}
