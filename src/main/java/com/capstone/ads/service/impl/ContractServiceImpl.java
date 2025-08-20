package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.NotificationMessage;
import com.capstone.ads.constaint.PaymentPolicy;
import com.capstone.ads.constaint.PredefinedRole;
import com.capstone.ads.constaint.S3ImageKeyFormat;
import com.capstone.ads.dto.contract.ContractDTO;
import com.capstone.ads.dto.contract.ContractRevisedRequest;
import com.capstone.ads.dto.contract.ContractSendRequest;
import com.capstone.ads.event.RoleNotificationEvent;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ContractMapper;
import com.capstone.ads.model.Contract;
import com.capstone.ads.model.Orders;
import com.capstone.ads.model.enums.ContractStatus;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.repository.internal.ContractRepository;
import com.capstone.ads.service.ContractService;
import com.capstone.ads.service.FileDataService;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.validator.ContractStateValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContractServiceImpl implements ContractService {
    FileDataService fileDataService;
    OrderService orderService;
    ContractMapper contractMapper;
    ContractRepository contractRepository;
    ContractStateValidator contractStateValidator;
    ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ContractDTO saleSendFirstContract(String orderId, ContractSendRequest request) {
        Orders orders = orderService.getOrderById(orderId);
        if (Objects.isNull(request.getDepositPercentChanged())) {
            request.setDepositPercentChanged(PaymentPolicy.DEPOSIT_PERCENT);
        }

        String contractUrl = uploadContractImageToS3(request.getContractNumber(), request.getContactFile());
        Contract contract = contractMapper.sendContract(request, contractUrl);
        contract.setOrders(orders);
        contract = contractRepository.save(contract);

        orderService.updateOrderStatus(orderId, OrderStatus.CONTRACT_SENT);
        return contractMapper.toDTO(contract);
    }

    @Override
    @Transactional
    public ContractDTO customerSendSingedContract(String contractId, MultipartFile singedContractFile) {
        Contract contract = getContractById(contractId);
        String orderId = contract.getOrders().getId();
        contractStateValidator.validateTransition(contract.getStatus(), ContractStatus.SIGNED);
        fileDataService.hardDeleteFileDataByImageUrl(contract.getSignedContractUrl());

        String singedContractUrl = uploadContractImageToS3(contract.getContractNumber(), singedContractFile);
        contractMapper.sendSingedContract(singedContractUrl, contract);
        contractRepository.save(contract);

        orderService.updateOrderStatus(orderId, OrderStatus.CONTRACT_SIGNED);
        eventPublisher.publishEvent(new RoleNotificationEvent(
                this,
                PredefinedRole.SALE_ROLE,
                String.format(NotificationMessage.CONTRACT_SINGED, contract.getOrders().getOrderCode())
        ));

        return contractMapper.toDTO(contract);
    }

    @Override
    @Transactional
    public ContractDTO saleSendRevisedContract(String contractId, ContractRevisedRequest request) {
        Contract contract = getContractById(contractId);
        String orderId = contract.getOrders().getId();
        contractStateValidator.validateTransition(contract.getStatus(), ContractStatus.SENT);
        fileDataService.hardDeleteFileDataByImageUrl(contract.getContractUrl());

        if (Objects.isNull(request.getDepositPercentChanged())) {
            request.setDepositPercentChanged(PaymentPolicy.DEPOSIT_PERCENT);
        }

        String contractUrl = uploadContractImageToS3(contract.getContractNumber(), request.getContactFile());
        contractMapper.sendRevisedContract(request.getDepositPercentChanged(), contractUrl, contract);
        contractRepository.save(contract);

        orderService.updateOrderStatus(orderId, OrderStatus.CONTRACT_SENT);
        return contractMapper.toDTO(contract);
    }

    @Override
    @Transactional
    public ContractDTO customerRequestDiscussForContract(String contractId) {
        Contract contract = getContractById(contractId);
        String orderId = contract.getOrders().getId();
        contractStateValidator.validateTransition(contract.getStatus(), ContractStatus.NEED_DISCUSS);

        contract.setStatus(ContractStatus.NEED_DISCUSS);
        contract = contractRepository.save(contract);

        eventPublisher.publishEvent(new RoleNotificationEvent(
                this,
                PredefinedRole.SALE_ROLE,
                String.format(NotificationMessage.CONTRACT_DISCUSS, contract.getOrders().getOrderCode())
        ));

        orderService.updateOrderStatus(orderId, OrderStatus.CONTRACT_DISCUSS);
        return contractMapper.toDTO(contract);
    }

    @Override
    public ContractDTO findContractByOrderId(String orderId) {
        var contract = contractRepository.findByOrders_Id(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
        return contractMapper.toDTO(contract);
    }

    @Override
    public Contract getContractById(String contractId) {
        return contractRepository.findById(contractId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
    }

    private String generateContractKey(String contractNumber) {
        return String.format(S3ImageKeyFormat.CONTRACT, contractNumber, UUID.randomUUID());
    }

    private String uploadContractImageToS3(String contractNumber, MultipartFile file) {
        String customDesignImageKey = generateContractKey(contractNumber);
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        fileDataService.uploadSingleFile(customDesignImageKey, file);
        return customDesignImageKey;
    }
}
