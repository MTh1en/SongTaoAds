package com.capstone.ads.service;

import com.capstone.ads.dto.contract.ContractDTO;
import com.capstone.ads.model.Contract;
import org.springframework.web.multipart.MultipartFile;

public interface ContractService {
    ContractDTO saleSendFirstContract(String orderId, String contractNumber, Long depositPercentChanged, MultipartFile contractFile);

    ContractDTO customerSendSingedContract(String contractId, MultipartFile signedContractFile);

    ContractDTO saleSendRevisedContract(String contractId, Long contractPercentChanged, MultipartFile contractFile);

    ContractDTO customerRequestDiscussForContract(String contractId);

    ContractDTO findContractByOrderId(String orderId);

    //INTERNAL FUNCTION

    Contract getContractById(String contractId);
}
