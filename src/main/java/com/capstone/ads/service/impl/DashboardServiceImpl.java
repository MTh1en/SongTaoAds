package com.capstone.ads.service.impl;

import com.capstone.ads.dto.dashboard.AdminDashboardResponse;
import com.capstone.ads.dto.dashboard.CustomerDashboardResponse;
import com.capstone.ads.dto.dashboard.SaleDashboardResponse;
import com.capstone.ads.dto.dashboard.StaffDashboardResponse;
import com.capstone.ads.model.Users;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.model.enums.TicketStatus;
import com.capstone.ads.repository.internal.*;
import com.capstone.ads.service.DashboardService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {
    private final OrdersRepository orderRepository;
    private final TicketRepository ticketRepository;
    private final PaymentsRepository paymentRepository;
    private final UsersRepository usersRepository;
    private final FeedbacksRepository feedbacksRepository;
    private final ContractRepository contractRepository;
    private final SecurityContextUtils securityContextUtils;

    @Override
    public CustomerDashboardResponse getCustomerDashboard() {
        Users user = securityContextUtils.getCurrentUser();
        int totalOrders = orderRepository.countByUsers(user);
        int contractConfirmedOrders = orderRepository.countByUsers_IdAndStatus(user.getId(), OrderStatus.CONTRACT_CONFIRMED);
        int depositedOrders = orderRepository.countByUsers_IdAndStatus(user.getId(), OrderStatus.DEPOSITED);
        int productionCompleteOrders = orderRepository.countByUsers_IdAndStatus(user.getId(), OrderStatus.PRODUCTION_COMPLETED);
        int completeOrders = orderRepository.countByUsers_IdAndStatus(user.getId(), OrderStatus.COMPLETED);
        int closeTickets = ticketRepository.countClosedByCustomer(user);
        long totalPayments = paymentRepository.countByOrders_Users_Id(user.getId());
        long totalDeposits = paymentRepository.countByIsDepositTrueAndOrders_Users_Id(user.getId());

        return CustomerDashboardResponse.builder()
                .totalOrders(totalOrders)
                .contractConfirmedOrders(contractConfirmedOrders)
                .depositedOrders(depositedOrders)
                .productionCompleteOrders(productionCompleteOrders)
                .completeOrders(completeOrders)
                .closeTickets(closeTickets)
                .totalPayments(totalPayments)
                .totalDeposits(totalDeposits)
                .build();
    }
    @Override
    public AdminDashboardResponse getAdminDashboard() {

        long totalOrders = orderRepository.count();
        long totalUsers = usersRepository.count();
        long totalRevenue = paymentRepository.sumAmount();
        int completedOrders = orderRepository.countByStatus(OrderStatus.COMPLETED);
        int activeContracts = orderRepository.countByStatus(OrderStatus.CONTRACT_CONFIRMED);

        return AdminDashboardResponse.builder()
                .totalOrders((int) totalOrders)
                .totalUsers((int) totalUsers)
                .totalRevenue(totalRevenue)
                .completedOrders(completedOrders)
                .activeContracts(activeContracts)
                .build();
    }

    @Override
    public SaleDashboardResponse getSaleDashboard() {

        long totalOrders = orderRepository.count();
        long totalPayments = paymentRepository.sumAmount();
        long totalDeposited = paymentRepository.sumDepositAmount();
        int totalPendingDesignOrders = orderRepository.countByStatus(OrderStatus.PENDING_DESIGN);
        int totalPendingContractOrders = orderRepository.countByStatus(OrderStatus.PENDING_CONTRACT);
        int totalDepositedOrders = orderRepository.countByStatus(OrderStatus.DEPOSITED);
        int pendingTickets = ticketRepository.countByStatus(TicketStatus.OPEN);
        long totalFeedback = feedbacksRepository.count();
        long totalContracts = contractRepository.count();

        return SaleDashboardResponse.builder()
                .totalOrders(totalOrders)
                .totalPayments(totalPayments)
                .totalDeposited(totalDeposited)
                .totalPendingDesignOrders(totalPendingDesignOrders)
                .totalPendingContractOrders(totalPendingContractOrders)
                .totalDepositedOrders(totalDepositedOrders)
                .pendingTickets(pendingTickets)
                .totalFeedback(totalFeedback)
                .totalContracts(totalContracts)
                .build();

    }

    @Override
    public StaffDashboardResponse getStaffDashboard() {

        int productingOrders = orderRepository.countByStatus(OrderStatus.PRODUCING);
        int productionCompletedOrders= orderRepository.countByStatus(OrderStatus.PRODUCTION_COMPLETED);
        int inprogressTickets = ticketRepository.countByStatus(TicketStatus.IN_PROGRESS);
        int completedOrders = orderRepository.countByStatus(OrderStatus.COMPLETED);

        return StaffDashboardResponse.builder()
                .productingOrders(productingOrders)
                .productionCompletedOrders(productionCompletedOrders)
                .inprogressTickets(inprogressTickets)
                .completedOrders(completedOrders)
                .build();
    }
}
