package com.capstone.ads.service;

import com.capstone.ads.dto.customer_choice_detail.CustomerChoicesDetailsDTO;

import java.util.List;

public interface CustomerChoiceDetailsService {
    CustomerChoicesDetailsDTO createCustomerChoiceDetail(String customerChoicesId, String attributeValueId);

    CustomerChoicesDetailsDTO updateAttributeValueInCustomerChoiceDetail(String customerChoiceDetailId, String attributeValueId);

    CustomerChoicesDetailsDTO findCustomerChoiceDetailById(String customerChoiceDetailId);

    List<CustomerChoicesDetailsDTO> findAllCustomerChoiceDetailByCustomerChoicesId(String customerChoicesId);

    void hardDeleteCustomerChoiceDetail(String customerChoiceDetailId);
}
