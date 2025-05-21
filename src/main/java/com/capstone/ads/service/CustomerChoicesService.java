package com.capstone.ads.service;

import com.capstone.ads.dto.customerchoice.CustomerChoicesDTO;

import java.util.List;

public interface CustomerChoicesService {
    CustomerChoicesDTO create(String customerId, String productTypeId);

    CustomerChoicesDTO finish(String customerId, String productTypeId);

    CustomerChoicesDTO findById(String customerChoiceId);

    List<CustomerChoicesDTO> findNewestByUserId(String userId);

    void delete(String id);
}
