package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeCreateRequest;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeDTO;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerChoicesSizeMapper;
import com.capstone.ads.model.CustomerChoicesSize;
import com.capstone.ads.repository.internal.CustomerChoicesRepository;
import com.capstone.ads.repository.internal.CustomerChoicesSizeRepository;
import com.capstone.ads.repository.internal.ProductTypeSizeRepository;
import com.capstone.ads.repository.internal.SizeRepository;
import com.capstone.ads.service.CustomerChoicesSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerChoicesSizeServiceImpl implements CustomerChoicesSizeService {
    private final CustomerChoicesSizeRepository customerChoicesSizeRepository;
    private final CustomerChoicesRepository customerChoicesRepository;
    private final SizeRepository sizeRepository;
    private final ProductTypeSizeRepository productTypeSizeRepository;
    private final CustomerChoicesSizeMapper customerChoicesSizeMapper;

    @Override
    @Transactional
    public CustomerChoicesSizeDTO create(String customerChoicesId, String sizeId, CustomerChoicesSizeCreateRequest request) {
        var customerChoice = customerChoicesRepository.findById(customerChoicesId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));

        if (!sizeRepository.existsById(sizeId))
            throw new AppException(ErrorCode.SIZE_NOT_FOUND);

        if (!productTypeSizeRepository.existsByProductType_IdAndSize_Id(customerChoice.getProductType().getId(), sizeId))
            throw new AppException(ErrorCode.SIZE_NOT_BELONG_PRODUCT_TYPE);
        CustomerChoicesSize customerChoicesSize = customerChoicesSizeMapper.toEntity(request, customerChoicesId, sizeId);
        customerChoicesSize = customerChoicesSizeRepository.save(customerChoicesSize);
        return customerChoicesSizeMapper.toDTO(customerChoicesSize); // createdAt and updatedAt may be null in response
    }

    @Override
    @Transactional
    public CustomerChoicesSizeDTO update(String customerChoicesId, String sizeId, CustomerChoicesSizeUpdateRequest request) {
        if (!customerChoicesRepository.existsById(customerChoicesId))
            throw new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND);

        if (!sizeRepository.existsById(sizeId))
            throw new AppException(ErrorCode.SIZE_NOT_FOUND);

        var customerChoicesSize = customerChoicesSizeRepository.findByCustomerChoices_IdAndSize_Id(customerChoicesId, sizeId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_SIZE_NOT_FOUND));
        customerChoicesSizeMapper.updateEntityFromRequest(request, customerChoicesSize);
        customerChoicesSize = customerChoicesSizeRepository.save(customerChoicesSize);
        return customerChoicesSizeMapper.toDTO(customerChoicesSize); // updatedAt may be null in response
    }

    @Override
    public CustomerChoicesSizeDTO findById(String id) {
        CustomerChoicesSize customerChoicesSize = customerChoicesSizeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_SIZE_NOT_FOUND));
        return customerChoicesSizeMapper.toDTO(customerChoicesSize);
    }

    @Override
    public List<CustomerChoicesSizeDTO> findAllByCustomerChoicesId(String customerChoicesId) {
        if (!customerChoicesRepository.existsById(customerChoicesId))
            throw new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND);

        return customerChoicesSizeRepository.findByCustomerChoices_Id(customerChoicesId).stream()
                .map(customerChoicesSizeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!customerChoicesSizeRepository.existsById(id)) {
            throw new AppException(ErrorCode.CUSTOMER_CHOICES_SIZE_NOT_FOUND);
        }
        customerChoicesSizeRepository.deleteById(id);
    }
}
