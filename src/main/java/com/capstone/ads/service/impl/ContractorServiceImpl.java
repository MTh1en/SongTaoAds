package com.capstone.ads.service.impl;

import com.capstone.ads.dto.contractor.ContractorCreateRequest;
import com.capstone.ads.dto.contractor.ContractorDTO;
import com.capstone.ads.dto.contractor.ContractorUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ContractorMapper;
import com.capstone.ads.model.Contractors;
import com.capstone.ads.repository.internal.ContractorsRepository;
import com.capstone.ads.service.ContractorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContractorServiceImpl implements ContractorService {
    ContractorMapper contractorMapper;
    ContractorsRepository contractorsRepository;

    @Override
    @Transactional
    public ContractorDTO createContractor(ContractorCreateRequest request) {
        Contractors contractor = contractorMapper.mapCreateRequestToEntity(request);
        contractor = contractorsRepository.save(contractor);
        return contractorMapper.toDTO(contractor);
    }

    @Override
    public ContractorDTO updateContractor(String contractorId, ContractorUpdateRequest request) {
        Contractors contractor = getContractorById(contractorId);
        contractorMapper.mapUpdateRequestToEntity(request, contractor);
        contractor = contractorsRepository.save(contractor);
        return contractorMapper.toDTO(contractor);
    }

    @Override
    public Page<ContractorDTO> findAllContractors(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return contractorsRepository.findByIsAvailable(true, pageable)
                .map(contractorMapper::toDTO);
    }

    @Override
    public Page<ContractorDTO> findAllContractorByIsInternal(int page, int size, boolean isInternal) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return contractorsRepository.findByIsInternalAndIsAvailable(isInternal, true, pageable)
                .map(contractorMapper::toDTO);
    }

    @Override
    public void hardDeleteContractor(String contractorId) {
        if (!contractorsRepository.existsById(contractorId)) {
            throw new AppException(ErrorCode.CONTRACTOR_NOT_FOUND);
        }
        contractorsRepository.deleteById(contractorId);
    }

    @Override
    public ContractorDTO findContractorById(String contractorId) {
        Contractors contractor = getContractorById(contractorId);
        return contractorMapper.toDTO(contractor);
    }

    //INTERNAL FUNCTION//

    @Override
    public Contractors getContractorById(String contractorId) {
        return contractorsRepository.findByIdAndIsAvailable(contractorId, true)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACTOR_NOT_FOUND));
    }
}
