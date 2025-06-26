package com.capstone.ads.service.impl;

import com.capstone.ads.dto.cost_type.CostTypeCreateRequest;
import com.capstone.ads.dto.cost_type.CostTypeDTO;
import com.capstone.ads.dto.cost_type.CostTypeUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CostTypeMapper;
import com.capstone.ads.model.CostTypes;
import com.capstone.ads.model.ProductTypes;
import com.capstone.ads.repository.internal.CostTypesRepository;
import com.capstone.ads.service.CostTypesService;
import com.capstone.ads.service.ProductTypesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CostTypesServiceImpl implements CostTypesService {
    private final ProductTypesService productTypesService;
    private final CostTypeMapper costTypeMapper;
    private final CostTypesRepository costTypesRepository;

    @Override
    @Transactional
    public CostTypeDTO createCostTypeByProductType(String productTypeId, CostTypeCreateRequest request) {
        ProductTypes productTypes = productTypesService.getProductTypeByIdAndAvailable(productTypeId);

        CostTypes costTypes = costTypeMapper.mapCreateRequestToEntity(request);
        costTypes.setProductTypes(productTypes);
        costTypesRepository.save(costTypes);

        return costTypeMapper.toDTO(costTypes);
    }

    @Override
    @Transactional
    public CostTypeDTO updateCostTypeInformation(String costTypeId, CostTypeUpdateRequest request) {
        CostTypes costTypes = getCostTypeByIdAndIsAvailable(costTypeId);

        costTypeMapper.mapUpdateRequestToEntity(request, costTypes);
        costTypesRepository.save(costTypes);

        return costTypeMapper.toDTO(costTypes);
    }

    @Override
    public List<CostTypeDTO> findCostTypeByProductType(String productTypeId) {
        return getCostTypesByProductTypeSortedByPriority(productTypeId).stream()
                .map(costTypeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CostTypeDTO> findAllCostTypes(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return costTypesRepository.findAll(pageable)
                .map(costTypeMapper::toDTO);
    }

    @Override
    @Transactional
    public void hardDeleteCostType(String costTypeId) {
        if (!costTypesRepository.existsById(costTypeId)) {
            throw new AppException(ErrorCode.COST_TYPE_NOT_FOUND);
        }
        costTypesRepository.deleteById(costTypeId);
    }

    @Override
    public CostTypes getCostTypeByIdAndIsAvailable(String costTypeId) {
        return costTypesRepository.findByIdAndIsAvailable(costTypeId, true)
                .orElseThrow(() -> new AppException(ErrorCode.COST_TYPE_NOT_FOUND));
    }

    @Override
    public CostTypes getCoreCostTypeByProductType(String productTypeId) {
        return costTypesRepository.findByProductTypes_IdAndIsCore(productTypeId, true)
                .orElseThrow(() -> new AppException(ErrorCode.COST_TYPE_NOT_FOUND));
    }

    @Override
    public List<CostTypes> getCostTypesByProductTypeSortedByPriority(String productTypeId) {
        return costTypesRepository.findByProductTypes_IdOrderByPriorityAsc(productTypeId);
    }
}
