package com.capstone.ads.mapper;

import com.capstone.ads.dto.contract.ContractDTO;
import com.capstone.ads.dto.contract.ContractSendRequest;
import com.capstone.ads.model.Contract;
import com.capstone.ads.model.enums.ContractStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    ContractDTO toDTO(Contract contract);

    @Mapping(target = "sentDate", expression = "java(initLocalDateTime())")
    @Mapping(target = "status", expression = "java(sendContractStatus())")
    Contract sendContract(ContractSendRequest request, String contractUrl);

    @Mapping(target = "signedDate", expression = "java(initLocalDateTime())")
    @Mapping(target = "status", expression = "java(sendSingedContractStatus())")
    void sendSingedContract(String signedContractUrl, @MappingTarget Contract contract);

    @Mapping(target = "sentDate", expression = "java(initLocalDateTime())")
    @Mapping(target = "status", expression = "java(sendContractStatus())")
    void sendRevisedContract(Long depositPercentChanged, String contractUrl, @MappingTarget Contract contract);

    default LocalDateTime initLocalDateTime() {
        return LocalDateTime.now();
    }

    default ContractStatus sendContractStatus() {
        return ContractStatus.SENT;
    }

    default ContractStatus sendSingedContractStatus() {
        return ContractStatus.SIGNED;
    }
}
