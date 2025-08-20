package com.capstone.ads.dto.order;

import com.capstone.ads.dto.feedback.FeedbackDTO;
import com.capstone.ads.dto.order_detail.OrderDetailDTO;
import com.capstone.ads.dto.payment.PaymentDTO;
import com.capstone.ads.dto.ticket.TicketDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderTrackingDTO {
    OrderDTO orders;
    List<PaymentDTO> payments;
    List<TicketDTO> tickets;
    List<FeedbackDTO> feedbacks;
    List<OrderDetailDTO> orderDetails;
}
