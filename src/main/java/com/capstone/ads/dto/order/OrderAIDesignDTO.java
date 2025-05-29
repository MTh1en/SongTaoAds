package com.capstone.ads.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderAIDesignDTO {
    String id;
    String image;
    String customerNote;
    LocalDateTime createAt;
    LocalDateTime updateAt;
}
