package com.capstone.ads.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @NotBlank(message = "Full name is required")
    String fullName;
    @Email(message = "Email invalid format")
    String email;
    @Size(min = 10, message = "Phone must be at least 10 characters")
    String phone;
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password;
    String avatar;
    Boolean isActive;
    String roleName;
}
