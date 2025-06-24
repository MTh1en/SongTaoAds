package com.capstone.ads.dto.ticket;

import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.model.enums.TicketSeverity;
import com.capstone.ads.model.enums.TicketStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketDTO {
    String id;
    String title;
    String description;
    TicketSeverity severity;
    String solution;
    TicketStatus status; // ticket status: Open, In Progress, Closed
    UserDTO staff;
    UserDTO user;
    OrderDTO orders;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
