package com.capstone.ads.service;

import com.capstone.ads.dto.ticket.TicketDTO;
import com.capstone.ads.dto.ticket.TicketReport;
import com.capstone.ads.dto.ticket.TicketRequest;

import java.util.List;

public interface TicketService {

    TicketDTO sendOrderTicket(TicketRequest request, String orderId);
    TicketDTO reportTicketByStaff(String ticketId, TicketReport reportDetails);
    TicketDTO reportTicketBySaleStaff(String ticketId, TicketReport reportDetails);
    TicketDTO viewTicketDetails(String ticketId);
    List<TicketDTO> viewTicketsSentByCustomer();
    List<TicketDTO> viewTicketsOfStaff();
    List<TicketDTO> viewAllTickets();
    List<TicketDTO> viewTicketsByUserId(String userId);
    TicketDTO deliveryTicket(String ticketId);
}
