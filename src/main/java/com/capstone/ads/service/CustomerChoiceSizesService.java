package com.capstone.ads.service;

import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeCreateRequest;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeDTO;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeUpdateRequest;

import java.util.List;

public interface CustomerChoiceSizesService {
    CustomerChoicesSizeDTO createCustomerChoiceSize(String customerChoicesId, String sizeId, CustomerChoicesSizeCreateRequest request);

    CustomerChoicesSizeDTO updateValueInCustomerChoiceSize(String customerChoiceSizeId, CustomerChoicesSizeUpdateRequest request);

    CustomerChoicesSizeDTO findCustomerChoiceSizeById(String customerChoiceSizeId);

    List<CustomerChoicesSizeDTO> findAllCustomerChoiceSizeByCustomerChoicesId(String customerChoicesId);

    void hardDeleteCustomerChoiceSize(String id);
}
