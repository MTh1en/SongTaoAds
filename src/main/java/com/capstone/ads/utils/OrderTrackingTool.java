package com.capstone.ads.utils;

import com.capstone.ads.dto.feedback.FeedbackDTO;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderTrackingDTO;
import com.capstone.ads.dto.order_detail.OrderDetailDTO;
import com.capstone.ads.dto.payment.PaymentDTO;
import com.capstone.ads.dto.ticket.TicketDTO;
import com.capstone.ads.mapper.*;
import com.capstone.ads.repository.internal.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderTrackingTool {
    OrdersRepository ordersRepository;
    PaymentsRepository paymentsRepository;
    TicketRepository ticketRepository;
    FeedbacksRepository feedbacksRepository;
    OrderDetailsRepository orderDetailsRepository;
    OrdersMapper ordersMapper;
    PaymentMapper paymentMapper;
    TicketsMapper ticketsMapper;
    FeedbackMapper feedbackMapper;
    OrderDetailMapper orderDetailMapper;
    ObjectMapper objectMapper;

    @Tool(
            name = "trackOrder",
            description = "Truy vấn thông tin đơn hàng từ cơ sở dữ liệu dựa trên mã đơn hàng."
    )
    public String trackOrder(@ToolParam(description = "Mã đơn hàng cần truy vấn") String orderCode) {
        try {
            log.info("tracking order tool used");
            OrderDTO order = ordersMapper.toDTO(ordersRepository.findByOrderCode(orderCode));
            List<PaymentDTO> payments = paymentsRepository.findByOrders_OrderCode(orderCode).stream()
                    .map(paymentMapper::toDTO)
                    .toList();
            List<TicketDTO> tickets = ticketRepository.findByOrders_OrderCode(orderCode).stream()
                    .map(ticketsMapper::toDTO)
                    .toList();
            List<FeedbackDTO> feedbacks = feedbacksRepository.findByOrders_OrderCode(orderCode).stream()
                    .map(feedbackMapper::toDTO)
                    .toList();
            List<OrderDetailDTO> orderDetails = orderDetailsRepository.findByOrders_OrderCode(orderCode).stream()
                    .map(orderDetailMapper::toDTO)
                    .toList();
            OrderTrackingDTO orderTrackingDTO = OrderTrackingDTO.builder()
                    .orders(order)
                    .payments(payments)
                    .tickets(tickets)
                    .feedbacks(feedbacks)
                    .orderDetails(orderDetails)
                    .build();
            return objectMapper.writeValueAsString(orderTrackingDTO);
        } catch (Exception e) {
            return "{\"error\": \"Lỗi khi xử lý thông tin đơn hàng.\"}";
        }
    }
}
