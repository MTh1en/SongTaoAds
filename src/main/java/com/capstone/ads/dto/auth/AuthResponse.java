package com.capstone.ads.dto.auth;

import com.capstone.ads.dto.user.UserDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {
    String accessToken;
    String refreshToken;
    UserDTO user;
}
