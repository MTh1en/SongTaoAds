package com.capstone.ads.service;

import com.capstone.ads.dto.ticket.TicketDTO;
import com.capstone.ads.dto.ticket.TicketRequest;

import java.util.List;

public interface TicketService {

    TicketDTO sendOrderTicket(TicketRequest request);
    TicketDTO reportTicket(String ticketId, String reportDetails);
    TicketDTO viewTicketDetails(String ticketId);
    List<TicketDTO> viewTicketsSentByCustomer();
    List<TicketDTO> viewTicketsOfStaff();
    List<TicketDTO> viewAllTickets();
    TicketDTO deliveryTicket(String ticketId);
}
