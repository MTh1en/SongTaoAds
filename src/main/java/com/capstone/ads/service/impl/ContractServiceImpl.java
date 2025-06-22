package com.capstone.ads.service.impl;

import com.capstone.ads.dto.contract.ContractDTO;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ContractMapper;
import com.capstone.ads.model.Contract;
import com.capstone.ads.model.Orders;
import com.capstone.ads.model.enums.ContractStatus;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.repository.internal.ContractRepository;
import com.capstone.ads.service.ContractService;
import com.capstone.ads.service.OrderService;
import com.capstone.ads.service.S3Service;
import com.capstone.ads.utils.ContractStateValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractServiceImpl implements ContractService {
    private final S3Service s3Service;
    private final OrderService orderService;
    private final ContractMapper contractMapper;
    private final ContractRepository contractRepository;
    private final ContractStateValidator contractStateValidator;

    @Override
    @Transactional
    public ContractDTO saleSendFirstContract(String orderId, String contractNumber, Long depositPercentChanged, MultipartFile contractFile) {
        Orders orders = orderService.getOrderById(orderId);

        String contractUrl = uploadContractImageToS3(contractNumber, contractFile);
        Contract contract = contractMapper.sendContract(contractNumber, depositPercentChanged, contractUrl);
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

        String singedContractUrl = uploadContractImageToS3(contract.getContractNumber(), singedContractFile);
        contractMapper.sendSingedContract(singedContractUrl, contract);
        contractRepository.save(contract);

        orderService.updateOrderStatus(orderId, OrderStatus.CONTRACT_SIGNED);
        return contractMapper.toDTO(contract);
    }

    @Override
    @Transactional
    public ContractDTO saleSendRevisedContract(String contractId, Long contractPercentChanged, MultipartFile contractFile) {
        Contract contract = getContractById(contractId);
        String orderId = contract.getOrders().getId();
        contractStateValidator.validateTransition(contract.getStatus(), ContractStatus.SENT);

        String contractUrl = uploadContractImageToS3(contract.getContractNumber(), contractFile);
        contractMapper.sendRevisedContract(contractUrl, contract);
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
        return String.format("contract/%s/%s", contractNumber, UUID.randomUUID());
    }

    private String uploadContractImageToS3(String contractNumber, MultipartFile file) {
        String customDesignImageKey = generateContractKey(contractNumber);
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        s3Service.uploadSingleFile(customDesignImageKey, file);
        return customDesignImageKey;
    }
}
