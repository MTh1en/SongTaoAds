package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiPagingResponse;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.ticket.TicketDTO;
import com.capstone.ads.dto.ticket.TicketReport;
import com.capstone.ads.dto.ticket.TicketRequest;
import com.capstone.ads.model.enums.TicketStatus;
import com.capstone.ads.service.TicketService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "TICKET")
public class TicketController {
    private final TicketService ticketService;

    @PostMapping("/orders/{orderId}/tickets")
    @Operation(summary = "Customer gửi ticket theo đơn hàng")
    public ApiResponse<TicketDTO> sendOrderTicket(
            @RequestBody TicketRequest request,
            @PathVariable String orderId) {
        var ticket = ticketService.sendOrderTicket(request, orderId);
        return ApiResponseBuilder.buildSuccessResponse("Order ticket sent successfully", ticket);
    }

    @PatchMapping("/tickets/{ticketId}/report/sale")
    @Operation(summary = "Sale gửi phản hồi cho ticket")
    public ApiResponse<TicketDTO> reportTicketBySaleStaff(
            @PathVariable("ticketId") String ticketId,
            @RequestBody TicketReport reportDetails) {
        var ticket = ticketService.reportTicketBySaleStaff(ticketId, reportDetails);
        return ApiResponseBuilder.buildSuccessResponse("Ticket reported successfully", ticket);
    }

    @PatchMapping("/tickets/{ticketId}/report/staff")
    @Operation(summary = "Staff gửi phản hồi cho ticket")
    public ApiResponse<TicketDTO> reportTicketByStaff(
            @PathVariable String ticketId,
            @RequestBody TicketReport reportDetails) {
        var ticket = ticketService.reportTicketByStaff(ticketId, reportDetails);
        return ApiResponseBuilder.buildSuccessResponse("Ticket reported successfully", ticket);
    }

    @PatchMapping("/tickets/{ticketId}/deliveryTicket")
    @Operation(summary = "Sale chuyển ticket cho staff")
    public ApiResponse<TicketDTO> deliveryTicket(
            @PathVariable String ticketId) {
        var ticket = ticketService.deliveryTicket(ticketId);
        return ApiResponseBuilder.buildSuccessResponse("Ticket delivered successfully", ticket);
    }

    @GetMapping("/tickets")
    @Operation(summary = "Sale xem tất cả ticket")
    public ApiPagingResponse<TicketDTO> viewAllTickets(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var tickets = ticketService.viewAllTickets(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Tickets retrieved successfully", tickets, page);
    }

    @GetMapping("/tickets/{ticketId}")
    @Operation(summary = "Xem chi tiết ticket")
    public ApiResponse<TicketDTO> viewTicketDetails(@PathVariable String ticketId) {
        var ticket = ticketService.viewTicketDetails(ticketId);
        return ApiResponseBuilder.buildSuccessResponse("Ticket details retrieved successfully", ticket);
    }

    @GetMapping("/tickets/customer")
    @Operation(summary = "Xem tất cả ticket theo trạng thái")
    public ApiPagingResponse<TicketDTO> viewTicketByStatus(
            @RequestParam TicketStatus status,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var tickets = ticketService.viewTicketByStatus(status, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Tickets sent by customer retrieved successfully", tickets, page);
    }

    @GetMapping("/tickets/staff")
    @Operation(summary = "Staff xem tất cả ticket liên quan production (Staff)")
    public ApiPagingResponse<TicketDTO> viewTicketsOfStaff(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var tickets = ticketService.viewTicketsOfStaff(page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Tickets for staff retrieved successfully", tickets, page);
    }

    @GetMapping("/users/{userId}/tickets")
    @Operation(summary = "Customer xem tickets của mình")
    public ApiPagingResponse<TicketDTO> viewTicketsOfUserId(
            @PathVariable String userId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        var tickets = ticketService.viewTicketsByUserId(userId, page, size);
        return ApiResponseBuilder.buildPagingSuccessResponse("Tickets retrieved successfully", tickets, page);
    }
}