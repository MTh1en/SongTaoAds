package com.capstone.ads.repository.internal;

import com.capstone.ads.model.FileData;
import com.capstone.ads.model.enums.FileTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, String> {
    Page<FileData> findByFileType(FileTypeEnum fileType, Pageable pageable);

    List<FileData> findByDemoDesigns_Id(String id);

    List<FileData> findByCustomDesignRequests_Id(String id);

    Optional<FileData> findByImageUrl(String imageUrl);

    void deleteByImageUrl(String imageUrl);
}