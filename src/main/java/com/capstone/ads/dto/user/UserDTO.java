package com.capstone.ads.dto.user;

import com.capstone.ads.dto.CoreDTO;
import com.capstone.ads.dto.role.RoleDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    String id;
    String fullName;
    String email;
    String password;
    String phone;
    String avatar;
    String address;
    Boolean isActive;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    CoreDTO roles;
}
