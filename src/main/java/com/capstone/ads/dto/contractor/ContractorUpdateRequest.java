package com.capstone.ads.dto.contractor;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractorUpdateRequest {
    @NotBlank(message = "Name is required")
    String name;
    @NotBlank(message = "Address is required")
    String address;
    @NotBlank(message = "Phone is required")
    String phone;
    @NotBlank(message = "Email is required")
    String email;
    Boolean isInternal;
    Boolean isAvailable;
}
