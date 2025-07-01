package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeCreateRequest;
import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeDTO;
import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerChoiceSizesMapper;
import com.capstone.ads.model.CustomerChoiceSizes;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.model.Sizes;
import com.capstone.ads.repository.internal.*;
import com.capstone.ads.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerChoiceSizesServiceImpl implements CustomerChoiceSizesService {
    private final CustomerChoicesService customerChoicesService;
    private final CustomerChoiceCostsService customerChoiceCostsService;
    private final SizeService sizeService;
    private final ProductTypeSizesService productTypeSizesService;
    private final CustomerChoiceDetailsService customerChoiceDetailsService;
    private final CustomerChoiceSizesRepository customerChoiceSizesRepository;
    private final CustomerChoiceSizesMapper customerChoiceSizesMapper;

    @Override
    @Transactional
    public CustomerChoicesSizeDTO createCustomerChoiceSize(String customerChoicesId, String sizeId, CustomerChoicesSizeCreateRequest request) {
        CustomerChoices customerChoice = customerChoicesService.getCustomerChoiceById(customerChoicesId);
        Sizes sizes = sizeService.getSizeByIdAndIsAvailable(sizeId);
        productTypeSizesService.validateProductTypeSizeExist(customerChoice.getProductTypes().getId(), sizeId);

        if (customerChoiceSizesRepository.existsByCustomerChoices_IdAndSizes_Id(customerChoicesId, sizeId))
            throw new AppException(ErrorCode.CUSTOMER_CHOICE_SIZE_EXISTED);

        CustomerChoiceSizes customerChoiceSizes = customerChoiceSizesMapper.mapCreateRequestToEntity(request);
        customerChoiceSizes.setCustomerChoices(customerChoice);
        customerChoiceSizes.setSizes(sizes);
        customerChoiceSizes = customerChoiceSizesRepository.save(customerChoiceSizes);

        return customerChoiceSizesMapper.toDTO(customerChoiceSizes);
    }

    @Override
    @Transactional
    public CustomerChoicesSizeDTO updateValueInCustomerChoiceSize(String customerChoiceSizeId, CustomerChoicesSizeUpdateRequest request) {
        var customerChoicesSize = getCustomerChoiceSizesById(customerChoiceSizeId);
        CustomerChoices customerChoice = customerChoicesSize.getCustomerChoices();

        customerChoiceSizesMapper.updateEntityFromRequest(request, customerChoicesSize);
        customerChoicesSize = customerChoiceSizesRepository.save(customerChoicesSize);

        customerChoice.getCustomerChoiceDetails().forEach(customerChoiceDetailsService::recalculateSubtotal);
        customerChoiceCostsService.calculateAllCosts(customerChoice);
        customerChoicesService.recalculateTotalAmount(customerChoice);
        return customerChoiceSizesMapper.toDTO(customerChoicesSize);
    }

    @Override
    public CustomerChoicesSizeDTO findCustomerChoiceSizeById(String customerChoiceSizeId) {
        CustomerChoiceSizes customerChoiceSizes = getCustomerChoiceSizesById(customerChoiceSizeId);
        return customerChoiceSizesMapper.toDTO(customerChoiceSizes);
    }

    @Override
    public List<CustomerChoicesSizeDTO> findAllCustomerChoiceSizeByCustomerChoicesId(String customerChoicesId) {
        customerChoicesService.validateCustomerChoiceExists(customerChoicesId);

        return customerChoiceSizesRepository.findByCustomerChoices_Id(customerChoicesId).stream()
                .map(customerChoiceSizesMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void hardDeleteCustomerChoiceSize(String id) {
        if (!customerChoiceSizesRepository.existsById(id)) {
            throw new AppException(ErrorCode.CUSTOMER_CHOICES_SIZE_NOT_FOUND);
        }
        customerChoiceSizesRepository.deleteById(id);
    }

    private CustomerChoiceSizes getCustomerChoiceSizesById(String customerChoiceSizeId) {
        return customerChoiceSizesRepository.findById(customerChoiceSizeId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_SIZE_NOT_FOUND));
    }
}
