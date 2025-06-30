package com.capstone.ads.service;

import com.capstone.ads.dto.ticket.TicketDTO;
import com.capstone.ads.dto.ticket.TicketReport;
import com.capstone.ads.dto.ticket.TicketRequest;
import com.capstone.ads.model.enums.TicketStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TicketService {
    TicketDTO sendOrderTicket(TicketRequest request, String orderId);

    TicketDTO reportTicketByStaff(String ticketId, TicketReport reportDetails);

    TicketDTO reportTicketBySaleStaff(String ticketId, TicketReport reportDetails);

    TicketDTO viewTicketDetails(String ticketId);

    Page<TicketDTO> viewTicketByStatus(TicketStatus status, int page, int size);

    Page<TicketDTO> viewTicketsOfStaff(int page, int size);

    Page<TicketDTO> viewAllTickets(int page, int size);

    Page<TicketDTO> viewTicketsByUserId(String userId, int page, int size);

    TicketDTO deliveryTicket(String ticketId);
}
