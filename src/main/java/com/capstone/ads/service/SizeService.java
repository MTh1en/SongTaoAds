package com.capstone.ads.service;

import com.capstone.ads.dto.size.SizeCreateRequest;
import com.capstone.ads.dto.size.SizeDTO;
import com.capstone.ads.dto.size.SizeUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SizeService {
    SizeDTO create(SizeCreateRequest request);

    SizeDTO update(String id, SizeUpdateRequest request);

    SizeDTO findById(String id);

    Page<SizeDTO> findAll(int page, int size);

    void delete(String id);
}
