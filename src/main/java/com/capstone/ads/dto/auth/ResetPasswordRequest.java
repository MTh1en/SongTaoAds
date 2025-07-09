package com.capstone.ads.dto.auth;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordRequest {
    String email;
    String code;
    @Size(min = 6, message = "Old Password must be at least 6 characters")
    String newPassword;
    @Size(min = 6, message = "New Password must be at least 6 characters")
    String confirmPassword;
}
