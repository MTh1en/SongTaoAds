package com.capstone.ads.service;

import com.capstone.ads.dto.contractor.ContractorCreateRequest;
import com.capstone.ads.dto.contractor.ContractorDTO;
import com.capstone.ads.dto.contractor.ContractorUpdateRequest;
import com.capstone.ads.model.Contractors;
import org.springframework.data.domain.Page;

public interface ContractorService {
    ContractorDTO createContractor(ContractorCreateRequest request);

    ContractorDTO updateContractor(String contractorId, ContractorUpdateRequest request);

    Page<ContractorDTO> findAllContractors(int page, int size);

    Page<ContractorDTO> findAllContractorByIsInternal(int page, int size, boolean isInternal);

    void hardDeleteContractor(String contractorId);

    ContractorDTO findContractorById(String contractorId);

    // INTERNAL FUNCTION //
    Contractors getContractorById(String contractorId);
}
