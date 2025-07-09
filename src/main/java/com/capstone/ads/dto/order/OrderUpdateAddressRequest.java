package com.capstone.ads.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUpdateAddressRequest {
    @NotBlank(message = "Address is required")
    String address;
    String note;
}
