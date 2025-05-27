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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SizesServiceImpl implements SizeService {
    private final SizesRepository sizesRepository;
    private final SizesMapper sizesMapper;

    @Override
    @Transactional
    public SizeDTO create(SizeCreateRequest request) {
        Sizes sizes = sizesMapper.toEntity(request);
        sizes = sizesRepository.save(sizes);
        return sizesMapper.toDTO(sizes);
    }

    @Override
    @Transactional
    public SizeDTO update(String id, SizeUpdateRequest request) {
        Sizes sizes = sizesRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND));
        sizesMapper.updateEntityFromRequest(request, sizes);
        sizes = sizesRepository.save(sizes);
        return sizesMapper.toDTO(sizes);
    }

    @Override
    public SizeDTO findById(String id) {
        Sizes sizes = sizesRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND));
        return sizesMapper.toDTO(sizes);
    }

    @Override
    public List<SizeDTO> findAll() {
        return sizesRepository.findAll().stream()
                .map(sizesMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!sizesRepository.existsById(id)) {
            throw new AppException(ErrorCode.SIZE_NOT_FOUND);
        }
        sizesRepository.deleteById(id);
    }
}
