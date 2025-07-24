package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeCreateRequest;
import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeDTO;
import com.capstone.ads.dto.customer_choice_size.CustomerChoicesSizeUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerChoiceSizesMapper;
import com.capstone.ads.model.*;
import com.capstone.ads.repository.internal.*;
import com.capstone.ads.service.*;
import com.capstone.ads.utils.DataConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerChoiceSizesServiceImpl implements CustomerChoiceSizesService {
    CustomerChoicesService customerChoicesService;
    CustomerChoiceCostsService customerChoiceCostsService;
    SizeService sizeService;
    ProductTypeSizesService productTypeSizesService;
    CustomerChoiceDetailsService customerChoiceDetailsService;
    CustomerChoiceSizesRepository customerChoiceSizesRepository;
    CustomerChoiceSizesMapper customerChoiceSizesMapper;
    DataConverter dataConverter;

    @Override
    @Transactional
    public CustomerChoicesSizeDTO createCustomerChoiceSize(String customerChoicesId, String sizeId, CustomerChoicesSizeCreateRequest request) {
        CustomerChoices customerChoice = customerChoicesService.getCustomerChoiceById(customerChoicesId);
        Sizes sizes = sizeService.getSizeByIdAndIsAvailable(sizeId);
        String productTypeId = customerChoice.getProductTypes().getId();

        productTypeSizesService.validateProductTypeSizeExist(productTypeId, sizeId);
        productTypeSizesService.validateProductTypeSizeMaxValueAndMinValue(productTypeId, sizeId, request.getSizeValue());
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
        String productTypeId = customerChoice.getProductTypes().getId();
        String sizeId = customerChoicesSize.getSizes().getId();

        productTypeSizesService.validateProductTypeSizeMaxValueAndMinValue(productTypeId, sizeId, request.getSizeValue());
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

    @Override
    public Long convertToPixel(String customerChoiceSizeId) {
        CustomerChoiceSizes customerChoiceSizes = getCustomerChoiceSizesById(customerChoiceSizeId);
        String sizeId = customerChoiceSizes.getSizes().getId();
        String productTypeId = customerChoiceSizes.getCustomerChoices().getProductTypes().getId();

        ProductTypeSizes productTypeSizes = productTypeSizesService.getProductTypeSizeByProductTypeIdAndSizeId(productTypeId, sizeId);

        return dataConverter.convertSizeValueToPixelValue(
                customerChoiceSizes.getSizeValue(),
                productTypeSizes.getMinValue(),
                productTypeSizes.getMaxValue()
        );
    }

    // INTERNAL FUNCTION //

    private CustomerChoiceSizes getCustomerChoiceSizesById(String customerChoiceSizeId) {
        return customerChoiceSizesRepository.findById(customerChoiceSizeId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_SIZE_NOT_FOUND));
    }
}
