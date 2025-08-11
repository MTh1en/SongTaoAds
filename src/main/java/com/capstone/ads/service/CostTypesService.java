package com.capstone.ads.service;

import com.capstone.ads.dto.cost_type.CostTypeCreateRequest;
import com.capstone.ads.dto.cost_type.CostTypeDTO;
import com.capstone.ads.dto.cost_type.CostTypeUpdateRequest;
import com.capstone.ads.model.CostTypes;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CostTypesService {
    CostTypeDTO createCostTypeByProductType(String productTypeId, CostTypeCreateRequest request);

    CostTypeDTO updateCostTypeInformation(String costTypeId, CostTypeUpdateRequest request);

    List<CostTypeDTO> findCostTypeByProductType(String productTypeId);

    Page<CostTypeDTO> findAllCostTypes(int page, int size);

    void hardDeleteCostType(String costTypeId);

    //INTERNAL FUNCTION//
    CostTypes getCostTypeByIdAndIsAvailable(String costTypeId);

    List<CostTypes> getCostTypesByProductTypeSortedByPriority(String productTypeId);

}
