package com.capstone.ads.dto.user;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @Size(min = 6, message = "Old Password must be at least 6 characters")
    String oldPassword;
    @Size(min = 6, message = "New Password must be at least 6 characters")
    String newPassword;
}
