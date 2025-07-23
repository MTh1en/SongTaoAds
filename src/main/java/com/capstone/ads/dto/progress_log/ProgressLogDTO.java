package com.capstone.ads.dto.progress_log;

import com.capstone.ads.dto.CoreDTO;
import com.capstone.ads.model.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProgressLogDTO {
    String id;
    String description;
    OrderStatus status;
    LocalDateTime createdAt;
    String createdBy;
    CoreDTO orders;
}
