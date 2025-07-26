package com.capstone.ads.service;

import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeCreateRequest;
import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeDTO;
import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeUpdateRequest;
import com.capstone.ads.dto.customer_choice_size.PixelConvertResponse;

import java.util.List;

public interface CustomerChoiceSizesService {
    CustomerChoicesSizeDTO createCustomerChoiceSize(String customerChoicesId, String sizeId, CustomerChoicesSizeCreateRequest request);

    CustomerChoicesSizeDTO updateValueInCustomerChoiceSize(String customerChoiceSizeId, CustomerChoicesSizeUpdateRequest request);

    CustomerChoicesSizeDTO findCustomerChoiceSizeById(String customerChoiceSizeId);

    List<CustomerChoicesSizeDTO> findAllCustomerChoiceSizeByCustomerChoicesId(String customerChoicesId);

    void hardDeleteCustomerChoiceSize(String id);

    PixelConvertResponse convertCustomerChoiceSizeToPixel(String customerChoiceId);

    //INTERNAL FUNCTION
}
