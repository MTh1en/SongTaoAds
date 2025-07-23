package com.capstone.ads.dto.contractor;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractorDTO {
    String id;
    String name;
    String address;
    String phone;
    String email;
    Boolean isInternal;
    Boolean isAvailable;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
