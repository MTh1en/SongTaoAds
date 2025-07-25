package com.capstone.ads.dto.customer_choice_size;

import com.capstone.ads.dto.CoreDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerChoicesSizeDTO {
    String id;
    Float sizeValue;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    CoreDTO customerChoices;
    CoreDTO sizes;
}
