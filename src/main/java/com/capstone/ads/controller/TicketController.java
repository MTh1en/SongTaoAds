package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.ticket.TicketDTO;
import com.capstone.ads.dto.ticket.TicketReport;
import com.capstone.ads.dto.ticket.TicketRequest;
import com.capstone.ads.service.TicketService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "TICKET")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/orders/{orderId}/tickets")
    public ApiResponse<TicketDTO> sendOrderTicket(
            @RequestBody TicketRequest request,
            @PathVariable String orderId) {
        TicketDTO ticket = ticketService.sendOrderTicket(request, orderId);
        return ApiResponseBuilder.buildSuccessResponse("Order ticket sent successfully", ticket);
    }

    @PostMapping("/tickets/{ticketId}/report/sale-staff")
    public ApiResponse<TicketDTO> reportTicketBySaleStaff(
            @PathVariable("ticketId") String ticketId,
            @RequestBody TicketReport reportDetails) {
        TicketDTO ticket = ticketService.reportTicketBySaleStaff(ticketId, reportDetails);
        return ApiResponseBuilder.buildSuccessResponse("Ticket reported successfully", ticket);
    }
    @PostMapping("/tickets/{ticketId}/report/staff")
    public ApiResponse<TicketDTO> reportTicketByStaff(
            @PathVariable String ticketId,
            @RequestBody TicketReport reportDetails) {
        TicketDTO ticket = ticketService.reportTicketByStaff(ticketId, reportDetails);
        return ApiResponseBuilder.buildSuccessResponse("Ticket reported successfully", ticket);
    }

    @PostMapping("/tickets/{ticketId}/deliveryTicket")
    public ApiResponse<TicketDTO> deliveryTicket(
            @PathVariable String ticketId) {
        TicketDTO ticket = ticketService.deliveryTicket(ticketId);
        return ApiResponseBuilder.buildSuccessResponse("Ticket delivered successfully", ticket);
    }

    @GetMapping("/tickets")
    public ApiResponse<List<TicketDTO>> viewAllTickets() {
        List<TicketDTO> tickets = ticketService.viewAllTickets();
        return ApiResponseBuilder.buildSuccessResponse("Tickets retrieved successfully", tickets);
    }

    @GetMapping("/tickets/{ticketId}")
    public ApiResponse<TicketDTO> viewTicketDetails(@PathVariable String ticketId) {
        TicketDTO ticket = ticketService.viewTicketDetails(ticketId);
        return ApiResponseBuilder.buildSuccessResponse("Ticket details retrieved successfully", ticket);
    }

    @GetMapping("/tickets/customer")
    public ApiResponse<List<TicketDTO>> viewTicketsSentByCustomer() {
        List<TicketDTO> tickets = ticketService.viewTicketsSentByCustomer();
        return ApiResponseBuilder.buildSuccessResponse("Tickets sent by customer retrieved successfully", tickets);
    }

    @GetMapping("/tickets/staff")
    public ApiResponse<List<TicketDTO>> viewTicketsOfStaff() {
        List<TicketDTO> tickets = ticketService.viewTicketsOfStaff();
        return ApiResponseBuilder.buildSuccessResponse("Tickets for staff retrieved successfully", tickets);
    }

    @GetMapping("/users/{userId}/tickets")
    public ApiResponse<List<TicketDTO>> viewTicketsOfUserId(@PathVariable String userId) {
        List<TicketDTO> tickets = ticketService.viewTicketsByUserId(userId);
        return ApiResponseBuilder.buildSuccessResponse("Tickets for staff retrieved successfully", tickets);

    }
    }