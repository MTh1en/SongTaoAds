package com.capstone.ads.dto.customer_choice;

import com.capstone.ads.dto.CoreDTO;
import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.model.Users;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerChoicesDTO {
    String id;
    Long totalAmount;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    UserDTO users;
    CoreDTO productTypes;
}
