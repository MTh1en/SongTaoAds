package com.capstone.ads.service;

import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeCreateRequest;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeDTO;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeUpdateRequest;

import java.util.List;

public interface CustomerChoicesSizeService {
    CustomerChoicesSizeDTO create(String customerChoicesId, String sizeId, CustomerChoicesSizeCreateRequest request);

    CustomerChoicesSizeDTO update(String customerChoiceSizeId, String sizeId, CustomerChoicesSizeUpdateRequest request);

    CustomerChoicesSizeDTO findById(String customerChoiceSizeId);

    List<CustomerChoicesSizeDTO> findAllByCustomerChoicesId(String customerChoicesId);

    void delete(String id);
}
