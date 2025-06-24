package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.ticket.TicketDTO;
import com.capstone.ads.dto.ticket.TicketRequest;
import com.capstone.ads.service.TicketService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "TICKET")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("")
    public ApiResponse<TicketDTO> sendOrderTicket(@RequestBody TicketRequest request) {
        TicketDTO ticket = ticketService.sendOrderTicket(request);
        return ApiResponseBuilder.buildSuccessResponse("Order ticket sent successfully", ticket);
    }

    @PostMapping("/{ticketId}/report")
    public ApiResponse<TicketDTO> reportTicket(
            @PathVariable String ticketId,
            @RequestParam String reportDetails) {
        TicketDTO ticket = ticketService.reportTicket(ticketId, reportDetails);
        return ApiResponseBuilder.buildSuccessResponse("Ticket reported successfully", ticket);
    }

    @PostMapping("/{ticketId}/deliveryTicket")
    public ApiResponse<TicketDTO> deliveryTicket(
            @PathVariable String ticketId) {
        TicketDTO ticket = ticketService.deliveryTicket(ticketId);
        return ApiResponseBuilder.buildSuccessResponse("Ticket delivered successfully", ticket);
    }

    @GetMapping("")
    public ApiResponse<List<TicketDTO>> viewAllTickets() {
        List<TicketDTO> tickets = ticketService.viewAllTickets();
        return ApiResponseBuilder.buildSuccessResponse("Tickets retrieved successfully", tickets);
    }

    @GetMapping("/{ticketId}")
    public ApiResponse<TicketDTO> viewTicketDetails(@PathVariable String ticketId) {
        TicketDTO ticket = ticketService.viewTicketDetails(ticketId);
        return ApiResponseBuilder.buildSuccessResponse("Ticket details retrieved successfully", ticket);
    }

    @GetMapping("/customer")
    public ApiResponse<List<TicketDTO>> viewTicketsSentByCustomer() {
        List<TicketDTO> tickets = ticketService.viewTicketsSentByCustomer();
        return ApiResponseBuilder.buildSuccessResponse("Tickets sent by customer retrieved successfully", tickets);
    }
    @GetMapping("/staff")
    public ApiResponse<List<TicketDTO>> viewTicketsOfStaff() {
        List<TicketDTO> tickets = ticketService.viewTicketsOfStaff();
        return ApiResponseBuilder.buildSuccessResponse("Tickets for staff retrieved successfully", tickets);
    }



}