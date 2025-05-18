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
    private final SizeRepository repository;
    private final SizeMapper mapper;

    @Override
    @Transactional
    public SizeDTO create(SizeCreateRequest request) {
        Size size = mapper.toEntity(request);
        size = repository.save(size);
        return mapper.toDTO(size);
    }

    @Override
    @Transactional
    public SizeDTO update(String id, SizeUpdateRequest request) {
        Size size = repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND));
        mapper.updateEntityFromRequest(request, size);
        size = repository.save(size);
        return mapper.toDTO(size);
    }

    @Override
    public SizeDTO findById(String id) {
        Size size = repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND));
        return mapper.toDTO(size);
    }

    @Override
    public List<SizeDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new AppException(ErrorCode.SIZE_NOT_FOUND);
        }
        repository.deleteById(id);
    }
}
