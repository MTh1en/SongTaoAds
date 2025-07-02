package com.capstone.ads.repository.internal;

import com.capstone.ads.model.FileData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, String> {
    Page<FileData> findByContentTypeIgnoreCase(String contentType, Pageable pageable);
}