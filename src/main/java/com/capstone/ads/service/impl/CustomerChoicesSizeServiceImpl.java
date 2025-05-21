package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeCreateRequest;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeDTO;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerChoicesSizeMapper;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.model.CustomerChoicesSize;
import com.capstone.ads.repository.internal.*;
import com.capstone.ads.service.CalculateService;
import com.capstone.ads.service.CustomerChoicesSizeService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerChoicesSizeServiceImpl implements CustomerChoicesSizeService {
    private final CustomerChoicesSizeRepository customerChoicesSizeRepository;
    final CustomerChoicesDetailsRepository customerChoicesDetailsRepository;
    private final CustomerChoicesRepository customerChoicesRepository;
    private final SizeRepository sizeRepository;
    private final ProductTypeSizeRepository productTypeSizeRepository;
    private final CustomerChoicesSizeMapper customerChoicesSizeMapper;
    private final CalculateService calculateService;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public CustomerChoicesSizeDTO create(String customerChoicesId, String sizeId, CustomerChoicesSizeCreateRequest request) {
        var customerChoice = customerChoicesRepository.findById(customerChoicesId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));
        if (!sizeRepository.existsById(sizeId))
            throw new AppException(ErrorCode.SIZE_NOT_FOUND);
        if (!productTypeSizeRepository.existsByProductType_IdAndSize_Id(customerChoice.getProductType().getId(), sizeId))
            throw new AppException(ErrorCode.SIZE_NOT_BELONG_PRODUCT_TYPE);
        if (customerChoicesSizeRepository.existsByCustomerChoices_IdAndSize_Id(customerChoicesId, sizeId))
            throw new AppException(ErrorCode.CUSTOMER_CHOICE_SIZE_EXISTED);

        CustomerChoicesSize customerChoicesSize = customerChoicesSizeMapper.toEntity(request, customerChoicesId, sizeId);
        customerChoicesSize = customerChoicesSizeRepository.save(customerChoicesSize);
        return customerChoicesSizeMapper.toDTO(customerChoicesSize);
    }

    @Override
    @Transactional
    public CustomerChoicesSizeDTO update(String customerChoiceSizeId, CustomerChoicesSizeUpdateRequest request) {
        var customerChoicesSize = customerChoicesSizeRepository.findById(customerChoiceSizeId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_SIZE_NOT_FOUND));
        customerChoicesSizeMapper.updateEntityFromRequest(request, customerChoicesSize);
        customerChoicesSize = customerChoicesSizeRepository.save(customerChoicesSize);

        CustomerChoices customerChoices = customerChoicesSize.getCustomerChoices();
        updateAllSubtotalsAndTotal(customerChoices);
        return customerChoicesSizeMapper.toDTO(customerChoicesSize);
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

    private void updateAllSubtotalsAndTotal(CustomerChoices customerChoices) {
        // 1. Cập nhật tất cả subtotal
        customerChoices.getCustomerChoicesDetails().forEach(detail -> {
            detail.setSubTotal(calculateService.calculateSubtotal(detail.getId()));
            customerChoicesDetailsRepository.save(detail);
        });

        // 2. Flush để đảm bảo tất cả thay đổi được ghi xuống DB
        entityManager.flush();

        // 3. Refresh customerChoices để có dữ liệu mới nhất
        entityManager.refresh(customerChoices);

        // 4. Tính toán và cập nhật total
        double total = calculateService.calculateTotal(customerChoices.getId());
        customerChoices.setTotalAmount(total);
        customerChoicesRepository.save(customerChoices);

        log.info("Updated all subtotals and total for customerChoices {}", customerChoices.getId());
    }
}
