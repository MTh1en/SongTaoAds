package com.capstone.ads.service;

import com.capstone.ads.dto.contract.ContractDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ContractService {
    ContractDTO saleSendFirstContract(String orderId, String contractNumber, MultipartFile contractFile);

    ContractDTO customerSendSingedContract(String contractId, MultipartFile signedContractFile);

    ContractDTO saleSendRevisedContract(String contractId, MultipartFile contractFile);

    ContractDTO customerRequestDiscussForContract(String contractId);

    ContractDTO findContractByOrderId(String orderId);
}
