package com.capstone.ads.service;

import com.capstone.ads.dto.customer_choice_cost.CustomerChoiceCostDTO;
import com.capstone.ads.model.CustomerChoiceCosts;
import com.capstone.ads.model.CustomerChoices;

import java.util.List;

public interface CustomerChoiceCostsService {
    List<CustomerChoiceCostDTO> findCustomerChoiceCostByCustomerChoice(String customerChoiceId);

    void calculateAllCosts(CustomerChoices customerChoice);

    //INTERNAL FUNCTION//
    List<CustomerChoiceCosts> getCustomerChoiceCostByCustomerChoiceId(String customerChoiceId);
}
