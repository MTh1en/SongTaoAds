package com.capstone.ads.service;

import com.capstone.ads.dto.size.SizeCreateRequest;
import com.capstone.ads.dto.size.SizeDTO;
import com.capstone.ads.dto.size.SizeUpdateRequest;

import java.util.List;

public interface SizeService {
    SizeDTO create(SizeCreateRequest request);

    SizeDTO update(String id, SizeUpdateRequest request);

    SizeDTO findById(String id);

    List<SizeDTO> findAll();

    void delete(String id);
}
