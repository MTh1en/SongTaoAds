package com.capstone.ads.service;

import com.capstone.ads.dto.customerchoicedetail.CustomerChoicesDetailsDTO;

import java.util.List;

public interface CustomerChoicesDetailsService {
    CustomerChoicesDetailsDTO create(String customerChoicesId, String attributeValueId);

    CustomerChoicesDetailsDTO updateAttributeValue(String customerChoiceDetailId, String attributeValueId);

    CustomerChoicesDetailsDTO findById(String id);

    List<CustomerChoicesDetailsDTO> findAllByCustomerChoicesId(String customerChoicesId);

    void delete(String id);
}
