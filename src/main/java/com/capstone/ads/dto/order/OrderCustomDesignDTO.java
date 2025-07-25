package com.capstone.ads.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCustomDesignDTO {
    String id;
    String requirements;
    String finalDesignImage;
    LocalDateTime createAt;
    LocalDateTime updateAt;
}
