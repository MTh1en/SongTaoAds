package com.capstone.ads.service.impl;

import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeCreateRequest;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeDTO;
import com.capstone.ads.dto.customerchoicesize.CustomerChoicesSizeUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.CustomerChoiceSizesMapper;
import com.capstone.ads.model.CustomerChoiceSizes;
import com.capstone.ads.model.CustomerChoices;
import com.capstone.ads.repository.internal.*;
import com.capstone.ads.service.CalculateService;
import com.capstone.ads.service.CustomerChoiceSizesService;
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
public class CustomerChoiceSizesServiceImpl implements CustomerChoiceSizesService {
    private final CustomerChoiceSizesRepository customerChoiceSizesRepository;
    final CustomerChoiceDetailsRepository customerChoiceDetailsRepository;
    private final CustomerChoicesRepository customerChoicesRepository;
    private final SizesRepository sizesRepository;
    private final ProductTypeSizesRepository productTypeSizesRepository;
    private final CustomerChoiceSizesMapper customerChoiceSizesMapper;
    private final CalculateService calculateService;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public CustomerChoicesSizeDTO create(String customerChoicesId, String sizeId, CustomerChoicesSizeCreateRequest request) {
        var customerChoice = customerChoicesRepository.findById(customerChoicesId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND));
        if (!sizesRepository.existsById(sizeId))
            throw new AppException(ErrorCode.SIZE_NOT_FOUND);
        if (!productTypeSizesRepository.existsByProductTypes_IdAndSizes_Id(customerChoice.getProductTypes().getId(), sizeId))
            throw new AppException(ErrorCode.SIZE_NOT_BELONG_PRODUCT_TYPE);
        if (customerChoiceSizesRepository.existsByCustomerChoices_IdAndSizes_Id(customerChoicesId, sizeId))
            throw new AppException(ErrorCode.CUSTOMER_CHOICE_SIZE_EXISTED);

        CustomerChoiceSizes customerChoiceSizes = customerChoiceSizesMapper.toEntity(request, customerChoicesId, sizeId);
        customerChoiceSizes = customerChoiceSizesRepository.save(customerChoiceSizes);
        return customerChoiceSizesMapper.toDTO(customerChoiceSizes);
    }

    @Override
    @Transactional
    public CustomerChoicesSizeDTO update(String customerChoiceSizeId, CustomerChoicesSizeUpdateRequest request) {
        var customerChoicesSize = customerChoiceSizesRepository.findById(customerChoiceSizeId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_SIZE_NOT_FOUND));
        customerChoiceSizesMapper.updateEntityFromRequest(request, customerChoicesSize);
        customerChoicesSize = customerChoiceSizesRepository.save(customerChoicesSize);

        CustomerChoices customerChoices = customerChoicesSize.getCustomerChoices();
        updateAllSubtotalsAndTotal(customerChoices);
        return customerChoiceSizesMapper.toDTO(customerChoicesSize);
    }

    @Override
    public CustomerChoicesSizeDTO findById(String id) {
        CustomerChoiceSizes customerChoiceSizes = customerChoiceSizesRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_CHOICES_SIZE_NOT_FOUND));
        return customerChoiceSizesMapper.toDTO(customerChoiceSizes);
    }

    @Override
    public List<CustomerChoicesSizeDTO> findAllByCustomerChoicesId(String customerChoicesId) {
        if (!customerChoicesRepository.existsById(customerChoicesId))
            throw new AppException(ErrorCode.CUSTOMER_CHOICES_NOT_FOUND);

        return customerChoiceSizesRepository.findByCustomerChoices_Id(customerChoicesId).stream()
                .map(customerChoiceSizesMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!customerChoiceSizesRepository.existsById(id)) {
            throw new AppException(ErrorCode.CUSTOMER_CHOICES_SIZE_NOT_FOUND);
        }
        customerChoiceSizesRepository.deleteById(id);
    }

    private void updateAllSubtotalsAndTotal(CustomerChoices customerChoices) {
        // 1. Cập nhật tất cả subtotal
        customerChoices.getCustomerChoiceDetails().forEach(detail -> {
            detail.setSubTotal(calculateService.calculateSubtotal(detail.getId()));
            customerChoiceDetailsRepository.save(detail);
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
