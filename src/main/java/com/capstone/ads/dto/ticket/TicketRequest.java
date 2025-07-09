package com.capstone.ads.dto.ticket;

import com.capstone.ads.model.Orders;
import com.capstone.ads.model.enums.TicketSeverity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketRequest {
    @NotBlank(message = "Title is required")
    String title;
    @NotBlank(message = "Description is required")
    String description;
    TicketSeverity severity;
}
