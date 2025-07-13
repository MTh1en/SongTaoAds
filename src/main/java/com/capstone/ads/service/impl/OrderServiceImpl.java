package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.PaymentPolicy;
import com.capstone.ads.dto.file.FileDataDTO;
import com.capstone.ads.dto.file.UploadMultipleOrderFileRequest;
import com.capstone.ads.dto.order.OrderConfirmRequest;
import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.order.OrderUpdateAddressRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.OrdersMapper;
import com.capstone.ads.model.*;
import com.capstone.ads.model.enums.ContractStatus;
import com.capstone.ads.model.enums.FileTypeEnum;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.repository.internal.OrdersRepository;
import com.capstone.ads.service.*;
import com.capstone.ads.validator.ContractStateValidator;
import com.capstone.ads.utils.CustomerChoiceHistoriesConverter;
import com.capstone.ads.validator.OrderStateValidator;
import com.capstone.ads.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final FileDataService fileDataService;
    private final OrdersRepository orderRepository;
    private final OrdersMapper orderMapper;
    private final SecurityContextUtils securityContextUtils;
    private final CustomerChoiceHistoriesConverter customerChoiceHistoriesConverter;
    private final OrderStateValidator orderStateValidator;
    private final ContractStateValidator contractStateValidator;
    private final CustomDesignRequestService customDesignRequestService;
    private final CustomerChoicesService customerChoicesService;
    private final EditedDesignService editedDesignService;

    @Override
    @Transactional
    public OrderDTO createOrderByCustomDesign(String customDesignRequestId) {
        CustomDesignRequests customDesignRequests = customDesignRequestService.getCustomDesignRequestById(customDesignRequestId);
        Users users = securityContextUtils.getCurrentUser();

        Orders orders = Orders.builder()
                .customDesignRequests(customDesignRequests)
                .users(users)
                .status(OrderStatus.PENDING_DESIGN)
                .build();
        orders.setCustomerChoiceHistories(customDesignRequests.getCustomerChoiceHistories());
        orders.setTotalAmount(customDesignRequests.getCustomerChoiceHistories().getTotalAmount());

        orderRepository.save(orders);
        return orderMapper.toDTO(orders);
    }

    @Override
    @Transactional
    public OrderDTO createOrderByEditedDesign(String editedDesignId, String customerChoiceId) {
        EditedDesigns editedDesigns = editedDesignService.getEditedDesignById(editedDesignId);
        CustomerChoices customerChoices = customerChoicesService.getCustomerChoiceById(customerChoiceId);
        Users users = securityContextUtils.getCurrentUser();

        Orders orders = Orders.builder()
                .editedDesigns(editedDesigns)
                .users(users)
                .status(OrderStatus.PENDING_CONTRACT)
                .build();
        orders.setTotalAmount(customerChoices.getTotalAmount());
        orders.setCustomerChoiceHistories(customerChoiceHistoriesConverter.convertToHistory(customerChoices));

        orders = orderRepository.save(orders);
        customerChoicesService.hardDeleteCustomerChoice(customerChoiceId);
        return orderMapper.toDTO(orders);
    }

    @Override
    @Transactional
    public OrderDTO saleRequestCustomerResignContract(String orderId) {
        Orders orders = getOrderById(orderId);
        orderStateValidator.validateTransition(orders.getStatus(), OrderStatus.CONTRACT_RESIGNED);
        contractStateValidator.validateTransition(orders.getContract().getStatus(), ContractStatus.NEED_RESIGNED);

        orders.setStatus(OrderStatus.CONTRACT_RESIGNED);
        orders.getContract().setStatus(ContractStatus.NEED_RESIGNED);
        orderRepository.save(orders);
        return orderMapper.toDTO(orders);
    }

    @Override
    public OrderDTO saleConfirmContractSigned(String orderId) {
        Orders orders = getOrderById(orderId);
        Contract contract = orders.getContract();
        orderStateValidator.validateTransition(orders.getStatus(), OrderStatus.CONTRACT_CONFIRMED);

        long depositAmount = (long) ((!contract.getDepositPercentChanged().equals(PaymentPolicy.DEPOSIT_PERCENT))
                ? orders.getTotalAmount() * ((double) contract.getDepositPercentChanged() / 100)
                : orders.getTotalAmount() * ((double) PaymentPolicy.DEPOSIT_PERCENT / 100));
        long remainingAmount = orders.getTotalAmount() - depositAmount;

        orders.setStatus(OrderStatus.CONTRACT_CONFIRMED);
        orders.setDepositAmount(depositAmount);
        orders.setRemainingAmount(remainingAmount);
        orderRepository.save(orders);

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
    public OrderDTO customerProvideAddress(String orderId, OrderUpdateAddressRequest request) {
        Orders orders = getOrderById(orderId);

        orderMapper.updateEntityFromUpdateInformationRequest(request, orders);
        orders = orderRepository.save(orders);

        return orderMapper.toDTO(orders);
    }

    @Override
    @Transactional
    public OrderDTO saleNotifyEstimateDeliveryDate(String orderId, OrderConfirmRequest request) {
        Orders orders = getOrderById(orderId);
        orderStateValidator.validateTransition(orders.getStatus(), OrderStatus.IN_PROGRESS);

        orderMapper.updateEntityFromConfirmRequest(request, orders);
        orders.setStatus(OrderStatus.IN_PROGRESS);
        orders = orderRepository.save(orders);

        return orderMapper.toDTO(orders);
    }

    @Override
    @Transactional
    public OrderDTO staffUpdateOrderProducing(String orderId, MultipartFile draftImage) {
        Orders orders = getOrderById(orderId);
        orderStateValidator.validateTransition(orders.getStatus(), OrderStatus.PRODUCING);

        String draftImageUrl = uploadOrderImageToS3(orderId, draftImage);
        orders.setDraftImageUrl(draftImageUrl);
        orders.setStatus(OrderStatus.PRODUCING);
        orders.setProductionStartDate(LocalDateTime.now());

        orderRepository.save(orders);
        return orderMapper.toDTO(orders);
    }

    @Override
    @Transactional
    public OrderDTO staffUpdateOrderProductionComplete(String orderId, MultipartFile productImage) {
        Orders orders = getOrderById(orderId);
        orderStateValidator.validateTransition(orders.getStatus(), OrderStatus.PRODUCTION_COMPLETED);

        String productImageUrl = uploadOrderImageToS3(orderId, productImage);
        orders.setProductImageUrl(productImageUrl);
        orders.setStatus(OrderStatus.PRODUCTION_COMPLETED);
        orders.setProductionEndDate(LocalDateTime.now());


        orderRepository.save(orders);
        return orderMapper.toDTO(orders);
    }

    @Override
    public OrderDTO staffUpdateOrderDelivering(String orderId, MultipartFile deliveryImage) {
        Orders orders = getOrderById(orderId);
        orderStateValidator.validateTransition(orders.getStatus(), OrderStatus.DELIVERING);

        String deliveryImageUrl = uploadOrderImageToS3(orderId, deliveryImage);
        orders.setDeliveryImageUrl(deliveryImageUrl);
        orders.setStatus(OrderStatus.DELIVERING);
        orders.setDeliveryStartDate(LocalDateTime.now());

        orderRepository.save(orders);
        return orderMapper.toDTO(orders);
    }

    @Override
    public OrderDTO staffUpdateOrderInstalled(String orderId, MultipartFile installedImage) {
        Orders orders = getOrderById(orderId);
        orderStateValidator.validateTransition(orders.getStatus(), OrderStatus.INSTALLED);

        String installedImageUrl = uploadOrderImageToS3(orderId, installedImage);
        orders.setInstallationImageUrl(installedImageUrl);
        orders.setStatus(OrderStatus.INSTALLED);
        orders.setActualDeliveryDate(LocalDateTime.now());

        orderRepository.save(orders);
        return orderMapper.toDTO(orders);
    }

    @Override
    public List<FileDataDTO> uploadOrderSubImages(String orderId, UploadMultipleOrderFileRequest request) {
        Orders orders = getOrderById(orderId);
        return fileDataService.uploadMultipleFiles(
                request.getFiles(),
                request.getFileType(),
                orders,
                FileData::setOrders,
                (id, size) -> generateOrderSumImageKey(orderId, request.getFileType(), size)
        );
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
    public Page<OrderDTO> findOrderByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return orderRepository.findByUsers_Id(userId, pageable).map(orderMapper::toDTO);
    }

    //INTERNAL FUNCTION//

    @Override
    public Orders getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void updateOrderStatus(String orderId, OrderStatus status) {
        Orders orders = getOrderById(orderId);
        orderStateValidator.validateTransition(orders.getStatus(), status);
        orders.setStatus(status);
        orderRepository.save(orders);
    }

    @Override
    @Transactional
    public void updateOrderFromWebhookResult(Orders orders, boolean isDeposit) {
        if (isDeposit) {
            orders.setStatus(OrderStatus.DEPOSITED);
            orders.setDepositPaidDate(LocalDateTime.now());
        } else {
            orders.setStatus(OrderStatus.COMPLETED);
            orders.setCompletionDate(LocalDateTime.now());
        }
        orderRepository.save(orders);
    }

    private String generateOrderImageKey(String orderId) {
        return String.format("order/%s/%s", orderId, UUID.randomUUID());
    }

    private List<String> generateOrderSumImageKey(String orderId, FileTypeEnum fileType, Integer amountKey) {
        List<String> keys = new ArrayList<>();
        IntStream.range(0, amountKey)
                .forEach(i -> keys.add(String.format("order/%s/%s/%s", orderId, fileType, UUID.randomUUID())));
        return keys;
    }


    private String uploadOrderImageToS3(String orderId, MultipartFile file) {
        String customDesignImageKey = generateOrderImageKey(orderId);
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        fileDataService.uploadSingleFile(customDesignImageKey, file);
        return customDesignImageKey;
    }
}
