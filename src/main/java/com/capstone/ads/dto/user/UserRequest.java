package com.capstone.ads.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String avatar;
    private Boolean isActive;
    private String roleName;
}
