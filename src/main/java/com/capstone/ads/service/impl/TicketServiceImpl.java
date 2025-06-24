package com.capstone.ads.service.impl;

import com.capstone.ads.dto.ticket.TicketDTO;
import com.capstone.ads.dto.ticket.TicketRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.OrdersMapper;
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
    public TicketDTO sendOrderTicket(TicketRequest request) {
        Users users = securityContextUtils.getCurrentUser();
        Orders orders = orderService.getOrderById(request.getOrderId());
        if (orders == null) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        Tickets ticket = ticketsMapper.sendTicket(request, users, orders);
        ticket = ticketRepository.save(ticket);

        return ticketsMapper.toDTO(ticket);
    }

    @Override
    public TicketDTO reportTicket(String ticketId, String solution) {
        Tickets ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
        if (ticket.getStatus() != TicketStatus.OPEN) {
            throw new AppException(ErrorCode.TICKET_NOT_OPEN);
        }
        Users currentUser = securityContextUtils.getCurrentUser();
        String userRole = currentUser.getRoles().getName();
        boolean isSolutionApplied = false;
        if (TicketSeverity.SALE.equals(ticket.getSeverity())) {
            if ("SALE_STAFF".equalsIgnoreCase(userRole)) {
                ticketsMapper.updateSolution(ticket, solution, currentUser);
                ticket.setStaff(currentUser);
                isSolutionApplied = true;
            }
        } else if (TicketSeverity.PRODUCTION.equals(ticket.getSeverity())) {
            if ("STAFF".equalsIgnoreCase(userRole)) {
                ticketsMapper.updateSolution(ticket, solution, currentUser);
                ticket.setStaff(currentUser);
                isSolutionApplied = true;
            }
        }
        if (!isSolutionApplied) {
            throw new AppException(ErrorCode.ROLE_NOT_AUTHORIZED);
        }
        ticket.setStatus(TicketStatus.CLOSED);
        ticket = ticketRepository.save(ticket);

        return ticketsMapper.toDTO(ticket);
    }

    @Override
    public TicketDTO deliveryTicket(String ticketId) {
        Tickets ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
        if (!TicketSeverity.SALE.equals(ticket.getSeverity())) {
            throw new AppException(ErrorCode.INVALID_SEVERITY);
        }
        Users currentUser = securityContextUtils.getCurrentUser();
        if (!"SALE_STAFF".equalsIgnoreCase(currentUser.getRoles().getName())) {
            throw new AppException(ErrorCode.ROLE_NOT_AUTHORIZED);
        }
        ticket.setStaff(null);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticket.setSeverity(TicketSeverity.PRODUCTION);
        ticket.setSolution(null);
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
        String userId = securityContextUtils.getCurrentUserId();
        // Assume tickets are linked to userId via staff or custom logic
        List<Tickets> tickets = ticketRepository.findByStatus(TicketStatus.OPEN);
        return tickets.stream()
                .filter(t -> t.getStaff() != null && t.getUser().getId().equals(userId))
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
    public List<TicketDTO> viewAllTickets() {
        List<Tickets> tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(ticketsMapper::toDTO)
                .collect(Collectors.toList());
    }



}
