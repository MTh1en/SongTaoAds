package com.capstone.ads.mapper;

import com.capstone.ads.dto.contractor.ContractorCreateRequest;
import com.capstone.ads.dto.contractor.ContractorDTO;
import com.capstone.ads.dto.contractor.ContractorUpdateRequest;
import com.capstone.ads.model.Contractors;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ContractorMapper {
    ContractorDTO toDTO(Contractors contractors);

    Contractors mapCreateRequestToEntity(ContractorCreateRequest request);

    void mapUpdateRequestToEntity(ContractorUpdateRequest request, @MappingTarget Contractors contractors);
}
