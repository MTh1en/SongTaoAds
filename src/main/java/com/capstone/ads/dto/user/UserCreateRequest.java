package com.capstone.ads.dto.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    String fullName;
    String email;
    String phone;
    String password;
    String avatar;
    Boolean isActive;
    String roleName;
}
