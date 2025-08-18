package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.PredefinedRole;
import com.capstone.ads.dto.dashboard.*;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.Payments;
import com.capstone.ads.model.enums.*;
import com.capstone.ads.repository.internal.*;
import com.capstone.ads.service.DashboardService;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardServiceImpl implements DashboardService {
    OrdersRepository orderRepository;
    TicketRepository ticketRepository;
    PaymentsRepository paymentRepository;
    UsersRepository usersRepository;
    FeedbacksRepository feedbacksRepository;
    ContractRepository contractRepository;
    FileDataRepository fileDataRepository;
    NotificationRepository notificationRepository;
    ChatBotLogRepository chatBotLogRepository;
    CustomDesignRequestsRepository customDesignRequestsRepository;
    ProductTypesRepository productTypesRepository;
    AttributesRepository attributesRepository;
    AttributeValuesRepository attributeValuesRepository;
    CostTypesRepository costTypesRepository;
    DesignTemplatesRepository designTemplatesRepository;
    BackgroundsRepository backgroundsRepository;
    ModelChatBotRepository modelChatBotRepository;
    TopicRepository topicRepository;
    QuestionRepository questionRepository;
    ContractorsRepository contractorsRepository;
    SecurityContextUtils securityContextUtils;

    @Override
    public AdminDashboardResponse getAdminDashboard() {
        int totalUser = Math.toIntExact(usersRepository.count());
        int totalBannedUser = usersRepository.countByIsBanned(true);
        int totalCustomer = usersRepository.countByRoles_Name(PredefinedRole.CUSTOMER_ROLE);
        int totalSale = usersRepository.countByRoles_Name(PredefinedRole.SALE_ROLE);
        int totalStaff = usersRepository.countByRoles_Name(PredefinedRole.STAFF_ROLE);
        int totalDesigner = usersRepository.countByRoles_Name(PredefinedRole.DESIGNER_ROLE);
        int totalAdmin = usersRepository.countByRoles_Name(PredefinedRole.ADMIN_ROLE);
        int totalPaymentTransactionCreated = Math.toIntExact(paymentRepository.count());
        int totalPaymentSuccess = paymentRepository.countByStatus(PaymentStatus.SUCCESS);
        int totalPaymentFailed = paymentRepository.countByStatus(PaymentStatus.FAILED);
        int totalPaymentCancelled = paymentRepository.countByStatus(PaymentStatus.CANCELLED);

        long totalPaymentSuccessAmount = paymentRepository.findByStatus(PaymentStatus.SUCCESS).parallelStream()
                .mapToLong(Payments::getAmount).sum();

        long totalPaymentFailureAmount = paymentRepository.findByStatus(PaymentStatus.FAILED).parallelStream()
                .mapToLong(Payments::getAmount).sum();

        long totalPaymentCancelledAmount = paymentRepository.findByStatus(PaymentStatus.CANCELLED).parallelStream()
                .mapToLong(Payments::getAmount).sum();

        long totalPayOSSuccessAmount = paymentRepository.findByStatusAndMethod(PaymentStatus.SUCCESS, PaymentMethod.PAYOS)
                .parallelStream()
                .mapToLong(Payments::getAmount).sum();

        long totalPayOSFailureAmount = paymentRepository.findByStatusAndMethod(PaymentStatus.FAILED, PaymentMethod.PAYOS)
                .parallelStream()
                .mapToLong(Payments::getAmount).sum();

        long totalPayOSCancelledAmount = paymentRepository.findByStatusAndMethod(PaymentStatus.CANCELLED, PaymentMethod.PAYOS)
                .parallelStream()
                .mapToLong(Payments::getAmount).sum();

        long totalCastAmount = paymentRepository.findByStatusAndMethod(PaymentStatus.SUCCESS, PaymentMethod.CAST)
                .parallelStream()
                .mapToLong(Payments::getAmount).sum();

        int totalImage = Math.toIntExact(fileDataRepository.count());
        int totalNotification = Math.toIntExact(notificationRepository.count());
        int totalChatBotUsed = Math.toIntExact(chatBotLogRepository.count());

        return AdminDashboardResponse.builder()
                .totalUsers(totalUser)
                .totalBannedUsers(totalBannedUser)
                .totalCustomer(totalCustomer)
                .totalSale(totalSale)
                .totalStaff(totalStaff)
                .totalDesigner(totalDesigner)
                .totalAdmin(totalAdmin)
                .totalPaymentTransactionCreated(totalPaymentTransactionCreated)
                .totalPaymentSuccess(totalPaymentSuccess)
                .totalPaymentFailure(totalPaymentFailed)
                .totalPaymentCancelled(totalPaymentCancelled)
                .totalPaymentSuccessAmount(totalPaymentSuccessAmount)
                .totalPaymentFailureAmount(totalPaymentFailureAmount)
                .totalPaymentCancelledAmount(totalPaymentCancelledAmount)
                .totalPayOSSuccessAmount(totalPayOSSuccessAmount)
                .totalPayOSFailureAmount(totalPayOSFailureAmount)
                .totalPayOSCancelledAmount(totalPayOSCancelledAmount)
                .totalCastAmount(totalCastAmount)
                .totalImage(totalImage)
                .totalNotification(totalNotification)
                .totalChatBotUsed(totalChatBotUsed)
                .build();
    }

    @Override
    public SaleDashboardResponse getSaleDashboard() {
        int totalOrder = Math.toIntExact(orderRepository.count());
        int totalOrderCompleted = orderRepository.countByStatus(OrderStatus.ORDER_COMPLETED);
        int totalOrderInProgress = orderRepository.countByStatusNotIn(List.of(
                OrderStatus.ORDER_COMPLETED,
                OrderStatus.CANCELLED)
        );
        int totalOrderCancelled = orderRepository.countByStatus(OrderStatus.CANCELLED);
        int totalAiDesignOrder = orderRepository.countByOrderTypeIn(List.of(OrderType.AI_DESIGN));
        int totalCustomDesignOrder = orderRepository.countByOrderTypeIn(List.of(
                OrderType.CUSTOM_DESIGN_WITH_CONSTRUCTION,
                OrderType.CUSTOM_DESIGN_WITHOUT_CONSTRUCTION
        ));

        int totalCustomDesignRequest = Math.toIntExact(customDesignRequestsRepository.count());
        int totalCustomDesignRequestCompleted = customDesignRequestsRepository.countByStatusIn(List.of(
                CustomDesignRequestStatus.COMPLETED
        ));
        int totalCustomDesignRequestInProgress = customDesignRequestsRepository.countByStatusNotIn(List.of(
                CustomDesignRequestStatus.COMPLETED,
                CustomDesignRequestStatus.CANCELLED
        ));
        int totalCustomDesignRequestCancelled = customDesignRequestsRepository.countByStatusIn(List.of(
                CustomDesignRequestStatus.CANCELLED
        ));

        long totalRevenue = paymentRepository.findByStatus(PaymentStatus.SUCCESS).parallelStream()
                .mapToLong(Payments::getAmount)
                .sum();
        long totalPayOSPayment = paymentRepository.findByStatusAndMethod(PaymentStatus.SUCCESS, PaymentMethod.PAYOS)
                .parallelStream()
                .mapToLong(Payments::getAmount)
                .sum();
        long totalCastPayment = paymentRepository.findByStatusAndMethod(PaymentStatus.SUCCESS, PaymentMethod.CAST)
                .parallelStream()
                .mapToLong(Payments::getAmount)
                .sum();
        long totalDesignPaid = paymentRepository.findByTypeInAndStatus(
                        List.of(PaymentType.DEPOSIT_DESIGN, PaymentType.REMAINING_DESIGN),
                        PaymentStatus.SUCCESS)
                .parallelStream()
                .mapToLong(Payments::getAmount)
                .sum();

        long totalOrderPaid = paymentRepository.findByTypeInAndStatus(
                        List.of(PaymentType.DEPOSIT_CONSTRUCTION, PaymentType.REMAINING_CONSTRUCTION),
                        PaymentStatus.SUCCESS)
                .parallelStream()
                .mapToLong(Payments::getAmount)
                .sum();

        int totalContractSigned = contractRepository.countByStatus(ContractStatus.CONFIRMED);
        int totalFeedback = Math.toIntExact(feedbacksRepository.count());
        int totalFeedbackResponse = Math.toIntExact(feedbacksRepository.findAll().parallelStream()
                .filter(feedbacks -> Objects.nonNull(feedbacks.getResponse())).count());

        int totalTicket = Math.toIntExact(ticketRepository.count());
        int totalTicketInProgress = ticketRepository.countByStatusIn(List.of(TicketStatus.OPEN, TicketStatus.IN_PROGRESS));
        int totalTicketClosed = ticketRepository.countByStatusIn(List.of(TicketStatus.CLOSED));
        int totalTicketDelivered = ticketRepository.countByStatusIn(List.of(TicketStatus.IN_PROGRESS));

        return SaleDashboardResponse.builder()
                .totalOrders(totalOrder)
                .totalOrderCompleted(totalOrderCompleted)
                .totalOrderInProgress(totalOrderInProgress)
                .totalOrderCancelled(totalOrderCancelled)
                .totalAiDesignOrder(totalAiDesignOrder)
                .totalCustomDesignOrder(totalCustomDesignOrder)
                .totalCustomDesignRequest(totalCustomDesignRequest)
                .totalCustomDesignRequestCompleted(totalCustomDesignRequestCompleted)
                .totalCustomDesignRequestInProgress(totalCustomDesignRequestInProgress)
                .totalCustomDesignRequestCancelled(totalCustomDesignRequestCancelled)
                .totalRevenue(totalRevenue)
                .totalPayOSPayment(totalPayOSPayment)
                .totalCastPayment(totalCastPayment)
                .totalDesignPaid(totalDesignPaid)
                .totalOrderPaid(totalOrderPaid)
                .totalContractSigned(totalContractSigned)
                .totalFeedback(totalFeedback)
                .totalFeedbackResponse(totalFeedbackResponse)
                .totalTicket(totalTicket)
                .totalTicketInProgress(totalTicketInProgress)
                .totalTicketClosed(totalTicketClosed)
                .totalTicketDelivered(totalTicketDelivered)
                .build();
    }

    @Override
    public StaffDashboardResponse getStaffDashboard() {
        int totalOrder = Math.toIntExact(orderRepository.count());
        int totalProducingOrder = orderRepository.countByStatus(OrderStatus.PRODUCING);
        int totalProductionCompletedOrder = orderRepository.countByStatus(OrderStatus.PRODUCTION_COMPLETED);
        int totalDeliveringOrder = orderRepository.countByStatus(OrderStatus.DELIVERING);
        int totalInstalledOrder = orderRepository.countByStatus(OrderStatus.INSTALLED);

        int totalProductType = Math.toIntExact(productTypesRepository.count());
        int totalProductTypeActive = productTypesRepository.countByIsAvailable(true);
        int totalProductTypeUsingAI = productTypesRepository.countByIsAiGenerated(true);

        int totalAttribute = Math.toIntExact(attributesRepository.count());
        int totalAttributeActive = attributesRepository.countByIsAvailable(true);

        int totalAttributeValue = Math.toIntExact(attributeValuesRepository.count());
        int totalAttributeValueActive = attributeValuesRepository.countByIsAvailable(true);

        int totalCostType = Math.toIntExact(costTypesRepository.count());
        int totalCostTypeActive = costTypesRepository.countByIsAvailable(true);

        int totalDesignTemplate = Math.toIntExact(designTemplatesRepository.count());
        int totalDesignTemplateActive = designTemplatesRepository.countByIsAvailable(true);

        int totalBackground = Math.toIntExact(backgroundsRepository.count());
        int totalBackgroundActive = backgroundsRepository.countByIsAvailable(true);

        int totalModelChatBot = Math.toIntExact(modelChatBotRepository.count());
        int totalTopic = Math.toIntExact(topicRepository.count());
        int totalQuestion = Math.toIntExact(questionRepository.count());

        int totalContractor = Math.toIntExact(contractorsRepository.count());
        int totalContractorActive = contractorsRepository.countByIsAvailable(true);
        int totalContactorInternal = contractorsRepository.countByIsInternal(true);
        int totalContractorExternal = contractorsRepository.countByIsInternal(false);

        long totalRevenue = paymentRepository.findByStatus(PaymentStatus.SUCCESS).parallelStream()
                .mapToLong(Payments::getAmount)
                .sum();
        long totalPayOSPayment = paymentRepository.findByStatusAndMethod(PaymentStatus.SUCCESS, PaymentMethod.PAYOS)
                .parallelStream()
                .mapToLong(Payments::getAmount)
                .sum();
        long totalCastPayment = paymentRepository.findByStatusAndMethod(PaymentStatus.SUCCESS, PaymentMethod.CAST)
                .parallelStream()
                .mapToLong(Payments::getAmount)
                .sum();

        return StaffDashboardResponse.builder()
                .totalOrder(totalOrder)
                .totalProducingOrder(totalProducingOrder)
                .totalProductionCompletedOrder(totalProductionCompletedOrder)
                .totalDeliveringOrder(totalDeliveringOrder)
                .totalInstalledOrder(totalInstalledOrder)
                .totalProductType(totalProductType)
                .totalProductTypeActive(totalProductTypeActive)
                .totalProductTypeUsingAI(totalProductTypeUsingAI)
                .totalAttribute(totalAttribute)
                .totalAttributeActive(totalAttributeActive)
                .totalAttributeValue(totalAttributeValue)
                .totalAttributeValueActive(totalAttributeValueActive)
                .totalCostType(totalCostType)
                .totalCostTypeActive(totalCostTypeActive)
                .totalDesignTemplate(totalDesignTemplate)
                .totalDesignTemplateActive(totalDesignTemplateActive)
                .totalBackground(totalBackground)
                .totalBackgroundActive(totalBackgroundActive)
                .totalModelChatBot(totalModelChatBot)
                .totalTopic(totalTopic)
                .totalQuestion(totalQuestion)
                .totalContractor(totalContractor)
                .totalContractorActive(totalContractorActive)
                .totalContactorInternal(totalContactorInternal)
                .totalContractorExternal(totalContractorExternal)
                .totalRevenue(totalRevenue)
                .totalPayOSPayment(totalPayOSPayment)
                .totalCastPayment(totalCastPayment)
                .build();
    }

    @Override
    public DesignerDashboardResponse getDesignerDashboard() {
        var currentDesigner = securityContextUtils.getCurrentUser();
        var customDesignRequestAssigned = customDesignRequestsRepository.findByAssignDesigner(currentDesigner);
        var allDemoDesigns = customDesignRequestAssigned.stream()
                .map(CustomDesignRequests::getDemoDesigns)
                .flatMap(List::stream)
                .toList();

        int totalCustomDesignRequestAssigned = customDesignRequestAssigned.size();
        int totalDemoSubmitted = allDemoDesigns.size();
        int totalDemoApproved = Math.toIntExact(allDemoDesigns.stream()
                .filter(demo -> demo.getStatus() == DemoDesignStatus.APPROVED)
                .count());
        int totalDemoRejected = Math.toIntExact(allDemoDesigns.stream()
                .filter(demo -> demo.getStatus() == DemoDesignStatus.REJECTED)
                .count());
        int totalFinalDesignSubmitted = Math.toIntExact(customDesignRequestAssigned.stream()
                .filter(request -> request.getStatus() == CustomDesignRequestStatus.COMPLETED)
                .count());

        return DesignerDashboardResponse.builder()
                .totalCustomDesignRequestAssigned(totalCustomDesignRequestAssigned)
                .totalDemoSubmitted(totalDemoSubmitted)
                .totalDemoApproved(totalDemoApproved)
                .totalDemoRejected(totalDemoRejected)
                .totalFinalDesignSubmitted(totalFinalDesignSubmitted)
                .build();
    }

    @Override
    public CustomRequestDashboardResponse customDesignRequestDashboard(TimeRangeRequest request) {
        LocalDateTime start = request.getStart();
        LocalDateTime end = request.getEnd();

        int total = customDesignRequestsRepository.countByUpdatedAtBetween(start, end);
        int pending = customDesignRequestsRepository.countByStatusAndUpdatedAtBetween(
                CustomDesignRequestStatus.PENDING,
                start, end
        );
        int pricingNotified = customDesignRequestsRepository.countByStatusAndUpdatedAtBetween(
                CustomDesignRequestStatus.PRICING_NOTIFIED,
                start, end
        );
        int rejectedPricing = customDesignRequestsRepository.countByStatusAndUpdatedAtBetween(
                CustomDesignRequestStatus.REJECTED_PRICING,
                start, end
        );
        int approvedPricing = customDesignRequestsRepository.countByStatusAndUpdatedAtBetween(
                CustomDesignRequestStatus.APPROVED_PRICING,
                start, end
        );
        int deposited = customDesignRequestsRepository.countByStatusAndUpdatedAtBetween(
                CustomDesignRequestStatus.DEPOSITED,
                start, end
        );
        int assignedDesigner = customDesignRequestsRepository.countByStatusAndUpdatedAtBetween(
                CustomDesignRequestStatus.ASSIGNED_DESIGNER,
                start, end
        );
        int processing = customDesignRequestsRepository.countByStatusAndUpdatedAtBetween(
                CustomDesignRequestStatus.PROCESSING,
                start, end
        );
        int designerRejected = customDesignRequestsRepository.countByStatusAndUpdatedAtBetween(
                CustomDesignRequestStatus.DESIGNER_REJECTED,
                start, end
        );
        int demoSubmitted = customDesignRequestsRepository.countByStatusAndUpdatedAtBetween(
                CustomDesignRequestStatus.DEMO_SUBMITTED,
                start, end
        );
        int revisionRequested = customDesignRequestsRepository.countByStatusAndUpdatedAtBetween(
                CustomDesignRequestStatus.REVISION_REQUESTED,
                start, end
        );
        int waitingFullPayment = customDesignRequestsRepository.countByStatusAndUpdatedAtBetween(
                CustomDesignRequestStatus.WAITING_FULL_PAYMENT,
                start, end
        );
        int fullyPaid = customDesignRequestsRepository.countByStatusAndUpdatedAtBetween(
                CustomDesignRequestStatus.FULLY_PAID,
                start, end
        );
        int completed = customDesignRequestsRepository.countByStatusAndUpdatedAtBetween(
                CustomDesignRequestStatus.COMPLETED,
                start, end
        );
        int cancelled = customDesignRequestsRepository.countByStatusAndUpdatedAtBetween(
                CustomDesignRequestStatus.CANCELLED,
                start, end
        );


        return CustomRequestDashboardResponse.builder()
                .total(total)
                .pending(pending)
                .pricingNotified(pricingNotified)
                .rejectedPricing(rejectedPricing)
                .approvedPricing(approvedPricing)
                .deposited(deposited)
                .assignedDesigner(assignedDesigner)
                .processing(processing)
                .designerRejected(designerRejected)
                .demoSubmitted(demoSubmitted)
                .revisionRequested(revisionRequested)
                .waitingFullPayment(waitingFullPayment)
                .fullyPaid(fullyPaid)
                .completed(completed)
                .cancelled(cancelled)
                .build();
    }

    @Override
    public OrderSaleDashboardResponse orderSaleDashboard(TimeRangeRequest request) {
        LocalDateTime start = request.getStart();
        LocalDateTime end = request.getEnd();

        int total = orderRepository.countByUpdatedAtBetween(start, end);
        int pendingDesign = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.PENDING_DESIGN,
                start, end
        );
        int needDepositDesign = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.NEED_DEPOSIT_DESIGN,
                start, end
        );
        int depositedDesign = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.DEPOSITED_DESIGN,
                start, end
        );
        int needFullyPaidDesign = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.NEED_FULLY_PAID_DESIGN,
                start, end
        );
        int waitingFinalDesign = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.WAITING_FINAL_DESIGN,
                start, end
        );
        int designCompleted = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.DESIGN_COMPLETED,
                start, end
        );
        int pendingContract = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.PENDING_CONTRACT,
                start, end
        );
        int contractSent = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.CONTRACT_SENT,
                start, end
        );
        int contractSigned = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.CONTRACT_SIGNED,
                start, end
        );
        int contractDiscuss = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.CONTRACT_DISCUSS,
                start, end
        );
        int contractResigned = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.CONTRACT_RESIGNED,
                start, end
        );
        int contractConfirmed = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.CONTRACT_CONFIRMED,
                start, end
        );
        int deposited = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.DEPOSITED,
                start, end
        );
        int inProgress = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.IN_PROGRESS,
                start, end
        );
        int cancelled = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.CANCELLED,
                start, end
        );

        return OrderSaleDashboardResponse.builder()
                .total(total)
                .pendingDesign(pendingDesign)
                .needDepositDesign(needDepositDesign)
                .depositedDesign(depositedDesign)
                .needFullyPaidDesign(needFullyPaidDesign)
                .waitingFinalDesign(waitingFinalDesign)
                .designCompleted(designCompleted)
                .pendingContract(pendingContract)
                .contractSent(contractSent)
                .contractConfirmed(contractConfirmed)
                .contractDiscuss(contractDiscuss)
                .contractSigned(contractSigned)
                .contractResigned(contractResigned)
                .contractConfirmed(contractConfirmed)
                .deposited(deposited)
                .inProgress(inProgress)
                .cancelled(cancelled)
                .build();
    }

    @Override
    public OrderStaffDashboardResponse orderStaffDashboard(TimeRangeRequest request) {
        LocalDateTime start = request.getStart();
        LocalDateTime end = request.getEnd();

        int total = orderRepository.countByUpdatedAtBetween(start, end);
        int producing = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.PRODUCING,
                start, end
        );
        int productionCompleted = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.PRODUCTION_COMPLETED,
                start, end
        );
        int delivering = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.DELIVERING,
                start, end
        );
        int installed = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.INSTALLED,
                start, end
        );
        int orderCompleted = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.ORDER_COMPLETED,
                start, end
        );
        int cancelled = orderRepository.countByStatusAndUpdatedAtBetween(
                OrderStatus.CANCELLED,
                start, end
        );

        return OrderStaffDashboardResponse.builder()
                .total(total)
                .producing(producing)
                .productionCompleted(productionCompleted)
                .delivering(delivering)
                .installed(installed)
                .orderCompleted(orderCompleted)
                .cancelled(cancelled)
                .build();
    }

    @Override
    public PaymentDashboardResponse paymentDashboard(TimeRangeRequest request) {
        LocalDateTime start = request.getStart();
        LocalDateTime end = request.getEnd();

        long revenue = paymentRepository.findByStatusAndUpdatedAtBetween(PaymentStatus.SUCCESS, start, end)
                .parallelStream()
                .mapToLong(Payments::getAmount)
                .sum();
        long payOSRevenue = paymentRepository.findByStatusAndMethodAndUpdatedAtBetween(
                        PaymentStatus.SUCCESS, PaymentMethod.PAYOS,
                        start, end)
                .parallelStream()
                .mapToLong(Payments::getAmount)
                .sum();
        long castRevenue = paymentRepository.findByStatusAndMethodAndUpdatedAtBetween(
                        PaymentStatus.SUCCESS, PaymentMethod.CAST,
                        start, end)
                .parallelStream()
                .mapToLong(Payments::getAmount)
                .sum();
        long designRevenue = paymentRepository.findByTypeInAndStatusAndUpdatedAtBetween(
                        List.of(PaymentType.DEPOSIT_DESIGN, PaymentType.REMAINING_DESIGN),
                        PaymentStatus.SUCCESS, start, end)
                .parallelStream()
                .mapToLong(Payments::getAmount)
                .sum();

        long constructionRevenue = paymentRepository.findByTypeInAndStatusAndUpdatedAtBetween(
                        List.of(PaymentType.DEPOSIT_CONSTRUCTION, PaymentType.REMAINING_CONSTRUCTION),
                        PaymentStatus.SUCCESS, start, end)
                .parallelStream()
                .mapToLong(Payments::getAmount)
                .sum();

        return PaymentDashboardResponse.builder()
                .revenue(revenue)
                .payOSRevenue(payOSRevenue)
                .castRevenue(castRevenue)
                .designRevenue(designRevenue)
                .constructionRevenue(constructionRevenue)
                .build();
    }
}
