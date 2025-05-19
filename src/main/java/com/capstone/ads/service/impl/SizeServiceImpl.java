package com.capstone.ads.service.impl;

import com.capstone.ads.dto.size.SizeCreateRequest;
import com.capstone.ads.dto.size.SizeDTO;
import com.capstone.ads.dto.size.SizeUpdateRequest;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.SizeMapper;
import com.capstone.ads.model.Size;
import com.capstone.ads.repository.internal.SizeRepository;
import com.capstone.ads.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {
    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;

    @Override
    @Transactional
    public SizeDTO create(SizeCreateRequest request) {
        Size size = sizeMapper.toEntity(request);
        size = sizeRepository.save(size);
        return sizeMapper.toDTO(size);
    }

    @Override
    @Transactional
    public SizeDTO update(String id, SizeUpdateRequest request) {
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND));
        sizeMapper.updateEntityFromRequest(request, size);
        size = sizeRepository.save(size);
        return sizeMapper.toDTO(size);
    }

    @Override
    public SizeDTO findById(String id) {
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND));
        return sizeMapper.toDTO(size);
    }

    @Override
    public List<SizeDTO> findAll() {
        return sizeRepository.findAll().stream()
                .map(sizeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!sizeRepository.existsById(id)) {
            throw new AppException(ErrorCode.SIZE_NOT_FOUND);
        }
        sizeRepository.deleteById(id);
    }
}
