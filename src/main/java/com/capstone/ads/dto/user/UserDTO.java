package com.capstone.ads.dto.user;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String avatar;
    private Boolean isActive;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String roleName;
}
