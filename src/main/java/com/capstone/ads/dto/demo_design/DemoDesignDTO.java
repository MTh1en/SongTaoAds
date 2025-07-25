package com.capstone.ads.dto.demo_design;

import com.capstone.ads.dto.CoreDTO;
import com.capstone.ads.model.enums.DemoDesignStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemoDesignDTO {
    String id;
    String designerDescription;
    String demoImage;
    String customerNote;
    String customerFeedbackImage;
    Integer version;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    DemoDesignStatus status;
    CoreDTO customDesignRequests;
}
