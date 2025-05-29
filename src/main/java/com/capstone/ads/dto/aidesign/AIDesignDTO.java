package com.capstone.ads.dto.aidesign;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AIDesignDTO {
    String id;
    String image;
    String customerNote;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    String customerDetail;
}
