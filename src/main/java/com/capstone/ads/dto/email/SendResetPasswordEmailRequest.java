package com.capstone.ads.dto.email;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendResetPasswordEmailRequest {
    @NotBlank(message = "Email is required")
    String email;
}
