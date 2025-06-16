package com.capstone.ads.service;

import com.capstone.ads.dto.customerchoice.CustomerChoicesDTO;

public interface CustomerChoicesService {
    CustomerChoicesDTO createCustomerChoice(String customerId, String productTypeId);

    CustomerChoicesDTO findCustomerChoiceById(String customerChoiceId);

    CustomerChoicesDTO findCustomerChoiceByUserId(String userId);

    void hardDeleteCustomerChoice(String id);
}
