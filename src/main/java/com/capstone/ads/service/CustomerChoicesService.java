package com.capstone.ads.service;

import com.capstone.ads.dto.customerchoice.CustomerChoicesCreateRequest;
import com.capstone.ads.dto.customerchoice.CustomerChoicesDTO;
import com.capstone.ads.dto.customerchoice.CustomerChoicesUpdateRequest;

import java.util.List;

public interface CustomerChoicesService {
    CustomerChoicesDTO create(String productTypeId, CustomerChoicesCreateRequest request);

    CustomerChoicesDTO update(String customerChoiceId, CustomerChoicesUpdateRequest request);

    CustomerChoicesDTO findById(String customerChoiceId);

    List<CustomerChoicesDTO> findNewestByUserId(String userId);

    void delete(String id);
}
