package com.capstone.ads.service;

import com.capstone.ads.dto.size.SizeCreateRequest;
import com.capstone.ads.dto.size.SizeDTO;
import com.capstone.ads.dto.size.SizeUpdateRequest;
import com.capstone.ads.model.Sizes;
import org.hibernate.engine.jdbc.Size;
import org.springframework.data.domain.Page;

public interface SizeService {
    SizeDTO createSize(SizeCreateRequest request);

    SizeDTO updateSizeInformation(String id, SizeUpdateRequest request);

    SizeDTO findSizeById(String id);

    Page<SizeDTO> findAllSize(int page, int size);

    void hardDeleteSize(String id);

    //INTERNAL FUNCTION
    Sizes getSizeByIdAndIsAvailable(String id);
}
