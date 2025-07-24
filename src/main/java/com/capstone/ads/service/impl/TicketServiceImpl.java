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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketServiceImpl implements TicketService {
    TicketRepository ticketRepository;
    TicketsMapper ticketsMapper;
    SecurityContextUtils securityContextUtils;
    OrderService orderService;

    @Override
    @Transactional
    public TicketDTO sendOrderTicket(TicketRequest request, String orderId) {
        Users users = securityContextUtils.getCurrentUser();
        Orders orders = orderService.getOrderById(orderId);

        Tickets ticket = ticketsMapper.sendTicket(request, users, orders);
        ticket = ticketRepository.save(ticket);
        return ticketsMapper.toDTO(ticket);
    }

    @Override
    @Transactional
    public TicketDTO reportTicketBySaleStaff(String ticketId, TicketReport report) {
        Tickets ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
        if (ticket.getStatus() != TicketStatus.OPEN) {
            throw new AppException(ErrorCode.TICKET_NOT_OPEN);
        }
        return reportTicket(report, ticket);
    }

    @Override
    @Transactional
    public TicketDTO reportTicketByStaff(String ticketId, TicketReport report) {
        Tickets ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new AppException(ErrorCode.TICKET_NOT_OPEN);
        }
        return reportTicket(report, ticket);
    }

    @Override
    @Transactional
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
    public Page<TicketDTO> viewTicketByStatus(TicketStatus status, int page, int size) {
        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return ticketRepository.findByStatus(status, pageable)
                .map(ticketsMapper::toDTO);
    }

    @Override
    public Page<TicketDTO> viewTicketsOfStaff(int page, int size) {
        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return ticketRepository.findBySeverity(TicketSeverity.PRODUCTION, pageable)
                .map(ticketsMapper::toDTO);
    }

    @Override
    public Page<TicketDTO> viewAllTickets(int page, int size) {
        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return ticketRepository.findAll(pageable)
                .map(ticketsMapper::toDTO);
    }

    @Override
    public Page<TicketDTO> viewTicketsByUserId(String userId, int page, int size) {
        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return ticketRepository.findByCustomerId(userId, pageable)
                .map(ticketsMapper::toDTO);
    }

    private TicketDTO reportTicket(TicketReport report, Tickets ticket) {
        String solution = report.getReport();
        Users currentUser = securityContextUtils.getCurrentUser();

        ticketsMapper.updateSolution(ticket, solution, currentUser);
        ticket.setStaff(currentUser);
        ticket.setStatus(TicketStatus.CLOSED);
        ticket = ticketRepository.save(ticket);

        return ticketsMapper.toDTO(ticket);
    }
}
