package com.capstone.ads.dto.order;

import com.capstone.ads.model.enums.DemoDesignStatus;
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
    String image;
    String customerNote;
    String designerDescription;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    DemoDesignStatus status;
}
