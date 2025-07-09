package com.capstone.ads.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileUpdateRequest {
    @NotBlank(message = "Full name is required")
    String fullName;
    @Size(min = 10, message = "Phone must be at least 10 characters")
    String phone;
}
