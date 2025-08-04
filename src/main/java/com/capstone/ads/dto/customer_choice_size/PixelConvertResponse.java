package com.capstone.ads.dto.customer_choice_size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PixelConvertResponse {
    Long width;
    Long height;
}
