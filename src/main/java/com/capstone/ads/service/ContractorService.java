package com.capstone.ads.service;

import com.capstone.ads.dto.contractor.ContractorCreateRequest;
import com.capstone.ads.dto.contractor.ContractorDTO;
import com.capstone.ads.dto.contractor.ContractorUpdateRequest;
import com.capstone.ads.model.Contractors;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ContractorService {
    ContractorDTO createContractor(ContractorCreateRequest request);

    ContractorDTO updateContractorInformation(String contractorId, ContractorUpdateRequest request);

    ContractorDTO updateContractorLogo(String contractorId, MultipartFile logoImage);

    Page<ContractorDTO> findAllContractors(int page, int size);

    Page<ContractorDTO> findAllContractorByIsInternal(int page, int size, boolean isInternal);

    void hardDeleteContractor(String contractorId);

    ContractorDTO findContractorById(String contractorId);

    // INTERNAL FUNCTION //
    Contractors getContractorById(String contractorId);
}
