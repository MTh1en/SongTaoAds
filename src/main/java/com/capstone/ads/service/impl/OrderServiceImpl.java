package com.capstone.ads.service.impl;

import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateInformationRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.OrdersMapper;
import com.capstone.ads.model.*;
import com.capstone.ads.model.enums.CustomDesignStatus;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.repository.internal.AIDesignsRepository;
import com.capstone.ads.repository.internal.CustomDesignsRepository;
import com.capstone.ads.repository.internal.CustomerChoicesRepository;
import com.capstone.ads.repository.internal.OrdersRepository;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.utils.CustomerChoiceHistoriesConverter;
import com.capstone.ads.utils.OrderStateValidator;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final CustomerChoicesRepository customerChoicesRepository;
    private final CustomDesignsRepository customDesignsRepository;
    private final AIDesignsRepository aiDesignsRepository;
    private final OrdersRepository orderRepository;
    private final OrdersMapper orderMapper;
    private final SecurityContextUtils securityContextUtils;
    private final CustomerChoiceHistoriesConverter customerChoiceHistoriesConverter;
    private final OrderStateValidator orderStateValidator;

    @Override
    @Transactional
    public OrderDTO createOrderByCustomDesign(String customDesignId) {
        CustomDesigns customDesigns = customDesignsRepository.findByIdAndStatus(customDesignId, CustomDesignStatus.APPROVED)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOM_DESIGN_NOT_FOUND));
        CustomDesignRequests customDesignRequests = customDesigns.getCustomDesignRequests();
        Users users = securityContextUtils.getCurrentUser();

        Orders orders = orderMapper.toEntityFromCreateOrderByCustomDesign(customDesigns, users);
        orders.setCustomerChoiceHistories(customDesignRequests.getCustomerChoiceHistories() != null
                ? customDesignRequests.getCustomerChoiceHistories()
                : null);
        orders.setTotalAmount(customDesignRequests.getCustomerChoiceHistories().getTotalAmount());
        orderRepository.save(orders);
        return orderMapper.toDTO(orders);
    }

    @Override
    @Transactional
    public OrderDTO createOrderByAIDesign(String customerChoiceId, String aiDesignId) {
        CustomerChoices customerChoice = findCustomerChoice(customerChoiceId);
        Users users = securityContextUtils.getCurrentUser();
        AIDesigns aiDesigns = aiDesignsRepository.findById(aiDesignId)
                .orElseThrow(() -> new AppException(ErrorCode.AI_DESIGN_NOT_FOUND));

        Orders orders = orderMapper.toEntityFromCreateOrderByAIDesign(aiDesigns, users);
        orders.setTotalAmount(customerChoice.getTotalAmount());
        orders.setCustomerChoiceHistories(customerChoiceHistoriesConverter.convertToHistory(customerChoice));

        orders = orderRepository.save(orders);
        customerChoicesRepository.deleteById(customerChoiceId);
        return orderMapper.toDTO(orders);
    }

    @Override
    public OrderDTO findOrderById(String orderId) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toDTO(orders);
    }

    @Override
    public List<OrderDTO> findOrderByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDTO customerUpdateOrderInformation(String orderId, OrderUpdateInformationRequest request) {
        Orders orders = orderRepository.findByIdAndStatus(orderId, OrderStatus.PENDING)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        orderMapper.updateEntityFromUpdateInformationRequest(request, orders);
        orders = orderRepository.save(orders);

        return orderMapper.toDTO(orders);
    }

    @Override
    @Transactional
    public OrderDTO saleConfirmOrder(String orderId, OrderConfirmRequest request) {
        Orders orders = orderRepository.findByIdAndStatus(orderId, OrderStatus.DEPOSITED)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        orderMapper.updateEntityFromConfirmRequest(request, orders);
        orders.setStatus(OrderStatus.PROCESSING);
        orders = orderRepository.save(orders);

        return orderMapper.toDTO(orders);
    }

    @Override
    @Transactional
    public void hardDeleteOrder(String orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        orderRepository.deleteById(orderId);
    }

    @Override
    @Transactional
    public OrderDTO changeOrderStatus(String orderId, OrderStatus newStatus) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        orderStateValidator.validateTransition(
                orders.getStatus(),
                newStatus
        );

        orders.setStatus(newStatus);
        orders = orderRepository.save(orders);
        return orderMapper.toDTO(orders);
    }

    @Override
    public List<OrderDTO> findOrderByUserId(String userId) {
        return orderRepository.findByUsers_Id(userId).stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    private CustomerChoices findCustomerChoice(String customerChoiceId) {
        return customerChoicesRepository.findById(customerChoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));
    }
}
