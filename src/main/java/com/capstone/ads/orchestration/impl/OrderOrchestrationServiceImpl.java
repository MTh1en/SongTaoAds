package com.capstone.ads.orchestration.impl;

import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateInformationRequest;
import com.capstone.ads.model.AIDesigns;
import com.capstone.ads.model.CustomDesignRequests;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.orchestration.OrderOrchestrationService;
import com.capstone.ads.service.AIDesignsService;
import com.capstone.ads.service.CustomDesignRequestService;
import com.capstone.ads.service.CustomerChoicesService;
import com.capstone.ads.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderOrchestrationServiceImpl implements OrderOrchestrationService {
    private final AIDesignsService aiDesignsService;
    private final OrderService orderService;
    private final CustomerChoicesService customerChoicesService;
    private final CustomDesignRequestService customDesignRequestService;

    @Override
    public OrderDTO createOrderByCustomDesignAndDeleteCustomerChoice(String CustomDesignRequestId, String customerChoiceId) {
        CustomerChoices customerChoices = customerChoicesService.getCustomerChoiceById(customerChoiceId);
        CustomDesignRequests customDesignRequests = customDesignRequestService.getCustomDesignRequestById(CustomDesignRequestId);
        var orderCreated = orderService.createOrderByCustomDesign(customDesignRequests, customerChoices);
        customerChoicesService.hardDeleteCustomerChoice(customerChoiceId);
        return orderCreated;
    }

    @Override
    @Transactional
    public OrderDTO createOrderByAIDesignAndDeleteCustomerChoice(String customerChoiceId, String aiDesignId) {
        CustomerChoices customerChoices = customerChoicesService.getCustomerChoiceById(customerChoiceId);
        AIDesigns aiDesigns = aiDesignsService.getAIDesignById(aiDesignId);
        var orderCreated = orderService.createOrderByAIDesign(customerChoices, aiDesigns);
        customerChoicesService.hardDeleteCustomerChoice(customerChoiceId);
        return orderCreated;
    }

    @Override
    public OrderDTO findOrderById(String orderId) {
        return orderService.findOrderById(orderId);
    }

    @Override
    public Page<OrderDTO> findOrderByStatus(OrderStatus status, int page, int size) {
        return orderService.findOrderByStatus(status, page, size);
    }

    @Override
    public OrderDTO customerUpdateOrderInformation(String orderId, OrderUpdateInformationRequest request) {
        return orderService.customerUpdateOrderInformation(orderId, request);
    }

    @Override
    public OrderDTO saleConfirmOrder(String orderId, OrderConfirmRequest request) {
        return orderService.saleConfirmOrder(orderId, request);
    }

    @Override
    public void hardDeleteOrder(String orderId) {
        orderService.hardDeleteOrder(orderId);
    }

    @Override
    public OrderDTO changeOrderStatus(String orderId, OrderStatus orderStatus) {
        return orderService.changeOrderStatus(orderId, orderStatus);
    }

    @Override
    public Page<OrderDTO> findOrderByUserId(String userId, int page, int size) {
        return orderService.findOrderByUserId(userId, page, size);
    }
}
