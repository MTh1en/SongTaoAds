package com.capstone.ads.dto.ticket;

import com.capstone.ads.model.Orders;
import com.capstone.ads.model.enums.TicketSeverity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketRequest {
    String title;
    String description;
    String orderId;
    TicketSeverity severity;
}
