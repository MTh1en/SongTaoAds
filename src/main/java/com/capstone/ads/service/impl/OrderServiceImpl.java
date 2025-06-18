package com.capstone.ads.service.impl;

import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateInformationRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.OrdersMapper;
import com.capstone.ads.model.*;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.repository.internal.OrdersRepository;
import com.capstone.ads.service.AIDesignsService;
import com.capstone.ads.service.CustomDesignRequestService;
import com.capstone.ads.service.CustomerChoicesService;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.utils.CustomerChoiceHistoriesConverter;
import com.capstone.ads.utils.OrderStateValidator;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrdersRepository orderRepository;
    private final OrdersMapper orderMapper;
    private final SecurityContextUtils securityContextUtils;
    private final CustomerChoiceHistoriesConverter customerChoiceHistoriesConverter;
    private final OrderStateValidator orderStateValidator;
    private final CustomDesignRequestService customDesignRequestService;
    private final CustomerChoicesService customerChoicesService;
    private final AIDesignsService aiDesignsService;

    @Override
    @Transactional
    public OrderDTO createOrderByCustomDesign(String customDesignRequestId, String customerChoiceId) {
        CustomDesignRequests customDesignRequests = customDesignRequestService.getCustomDesignRequestById(customDesignRequestId);
        CustomerChoices customerChoices = customerChoicesService.getCustomerChoiceById(customerChoiceId);
        Users users = securityContextUtils.getCurrentUser();

        Orders orders = orderMapper.toEntityFromCreateOrderByCustomDesign(customDesignRequests, users);
        orders.setCustomerChoiceHistories(customDesignRequests.getCustomerChoiceHistories() != null
                ? customDesignRequests.getCustomerChoiceHistories()
                : null);
        orders.setTotalAmount(customDesignRequests.getCustomerChoiceHistories().getTotalAmount());
        orders.setCustomerChoiceHistories(customerChoiceHistoriesConverter.convertToHistory(customerChoices));

        orderRepository.save(orders);
        return orderMapper.toDTO(orders);
    }

    @Override
    @Transactional
    public OrderDTO createOrderByAIDesign(String aiDesignId, String customerChoiceId) {
        AIDesigns aiDesigns = aiDesignsService.getAIDesignById(aiDesignId);
        CustomerChoices customerChoices = customerChoicesService.getCustomerChoiceById(customerChoiceId);
        Users users = securityContextUtils.getCurrentUser();

        Orders orders = orderMapper.toEntityFromCreateOrderByAIDesign(aiDesigns, users);
        orders.setTotalAmount(customerChoices.getTotalAmount());
        orders.setCustomerChoiceHistories(customerChoiceHistoriesConverter.convertToHistory(customerChoices));

        orders = orderRepository.save(orders);
        return orderMapper.toDTO(orders);
    }

    @Override
    public OrderDTO findOrderById(String orderId) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toDTO(orders);
    }

    @Override
    public Page<OrderDTO> findOrderByStatus(OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return orderRepository.findByStatus(status, pageable)
                .map(orderMapper::toDTO);
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
    public Page<OrderDTO> findOrderByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return orderRepository.findByUsers_Id(userId, pageable).map(orderMapper::toDTO);
    }
}
