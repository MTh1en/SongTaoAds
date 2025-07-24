package com.capstone.ads.dto.chatBot;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModernBillboardRequest {
    String frame;
    String background;
    String border;
    String textAndLogo;
    String textSpecification;
    String installationMethod;
    double height;
    double width;
    double textSize;
}
