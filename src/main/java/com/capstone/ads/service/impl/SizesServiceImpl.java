package com.capstone.ads.service.impl;

import com.capstone.ads.dto.size.SizeCreateRequest;
import com.capstone.ads.dto.size.SizeDTO;
import com.capstone.ads.dto.size.SizeUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.SizesMapper;
import com.capstone.ads.model.Sizes;
import com.capstone.ads.repository.internal.SizesRepository;
import com.capstone.ads.service.SizeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SizesServiceImpl implements SizeService {
    SizesRepository sizesRepository;
    SizesMapper sizesMapper;

    @Override
    @Transactional
    public SizeDTO createSize(SizeCreateRequest request) {
        Sizes sizes = sizesMapper.mapCreateRequestToEntity(request);
        sizes = sizesRepository.save(sizes);
        return sizesMapper.toDTO(sizes);
    }

    @Override
    @Transactional
    public SizeDTO updateSizeInformation(String id, SizeUpdateRequest request) {
        Sizes sizes = sizesRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND));
        sizesMapper.updateEntityFromRequest(request, sizes);
        sizes = sizesRepository.save(sizes);
        return sizesMapper.toDTO(sizes);
    }

    @Override
    public SizeDTO findSizeById(String id) {
        Sizes sizes = sizesRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND));
        return sizesMapper.toDTO(sizes);
    }

    @Override
    public Page<SizeDTO> findAllSize(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return sizesRepository.findAll(pageable)
                .map(sizesMapper::toDTO);
    }

    @Override
    @Transactional
    public void hardDeleteSize(String id) {
        if (!sizesRepository.existsById(id)) {
            throw new AppException(ErrorCode.SIZE_NOT_FOUND);
        }
        sizesRepository.deleteById(id);
    }

    @Override
    public Sizes getSizeByIdAndIsAvailable(String sizeId) {
        return sizesRepository.findByIdAndIsAvailable(sizeId, true)
                .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND));
    }
}
