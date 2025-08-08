package com.capstone.ads.dto.fine_tune;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileDeletionResponse {
    String id;
    String object;
    boolean deleted;
}
