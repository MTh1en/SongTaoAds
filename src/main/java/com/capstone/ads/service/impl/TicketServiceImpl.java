package com.capstone.ads.service.impl;

import com.capstone.ads.dto.ticket.TicketDTO;
import com.capstone.ads.dto.ticket.TicketReport;
import com.capstone.ads.dto.ticket.TicketRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.TicketsMapper;
import com.capstone.ads.model.Orders;
import com.capstone.ads.model.Tickets;
import com.capstone.ads.model.Users;
import com.capstone.ads.model.enums.TicketSeverity;
import com.capstone.ads.model.enums.TicketStatus;
import com.capstone.ads.repository.internal.TicketRepository;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.service.TicketService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final TicketsMapper ticketsMapper;
    private final SecurityContextUtils securityContextUtils;
    private final OrderService orderService;

    @Override
    public TicketDTO sendOrderTicket(TicketRequest request, String orderId) {
        Users users = securityContextUtils.getCurrentUser();
        Orders orders = orderService.getOrderById(orderId);
        Tickets ticket = ticketsMapper.sendTicket(request, users, orders);
        ticket = ticketRepository.save(ticket);

        return ticketsMapper.toDTO(ticket);
    }

    @Override
    public TicketDTO reportTicketBySaleStaff(String ticketId, TicketReport report) {
        Tickets ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
        if (ticket.getStatus() != TicketStatus.OPEN) {
            throw new AppException(ErrorCode.TICKET_NOT_OPEN);}
        return getTicketDTO(report, ticket);
    }

    @Override
    public TicketDTO reportTicketByStaff(String ticketId, TicketReport report) {
        Tickets ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
        if (ticket.getStatus() != TicketStatus.IN_PROGRESS) {
            throw new AppException(ErrorCode.ROLE_NOT_AUTHORIZED);}
        return getTicketDTO(report, ticket);
    }

    @Override
    public TicketDTO deliveryTicket(String ticketId) {
        Tickets ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
        if (!TicketSeverity.SALE.equals(ticket.getSeverity())) {
            throw new AppException(ErrorCode.INVALID_SEVERITY);
        }
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticket.setSeverity(TicketSeverity.PRODUCTION);
        ticket = ticketRepository.save(ticket);
        return ticketsMapper.toDTO(ticket);
    }

    @Override
    public TicketDTO viewTicketDetails(String ticketId) {
        Tickets ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
        return ticketsMapper.toDTO(ticket);
    }

    @Override
    public List<TicketDTO> viewTicketsSentByCustomer() {
        List<Tickets> tickets = ticketRepository.findByStatus(TicketStatus.OPEN);
        return tickets.stream()
                .map(ticketsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> viewTicketsOfStaff() {
        List<Tickets> tickets = ticketRepository.findBySeverity(TicketSeverity.PRODUCTION);
        return tickets.stream()
                .map(ticketsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<TicketDTO> viewAllTickets(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return ticketRepository.findAll(pageable)
                .map(ticketsMapper::toDTO);
    }

    @Override
    public List<TicketDTO> viewTicketsByUserId(String userId) {
        List<Tickets> tickets = ticketRepository.findByCustomerId(userId);
        return tickets.stream()
                .map(ticketsMapper::toDTO)
                .collect(Collectors.toList());
    }

    private TicketDTO getTicketDTO(TicketReport report, Tickets ticket) {
        String solution= report.getReport();
        Users currentUser = securityContextUtils.getCurrentUser();
        ticketsMapper.updateSolution(ticket, solution, currentUser);
        ticket.setStaff(currentUser);
        ticket.setStatus(TicketStatus.CLOSED);
        ticket = ticketRepository.save(ticket);
        return ticketsMapper.toDTO(ticket);
    }
}
