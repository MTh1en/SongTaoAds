package com.capstone.ads.dto.chatBot;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TraditionalBillboardRequest {
     String frame;
     String background;
     String border;
     int numberOfFaces;
     String installationMethod;
     String billboardFace;
     double height;
     double width;
}
