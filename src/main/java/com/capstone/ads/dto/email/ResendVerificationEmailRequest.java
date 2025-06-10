package com.capstone.ads.dto.email;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResendVerificationEmailRequest {
    @NotBlank(message = "Full Name is required")
    String fullName;
    @NotBlank(message = "Email is required")
    String email;
}
