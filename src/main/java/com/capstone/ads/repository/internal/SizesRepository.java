package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Sizes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SizesRepository extends JpaRepository<Sizes, String> {
    Page<Sizes> findByIsAvailable(Boolean isAvailable, Pageable pageable);

    Optional<Sizes> findByIdAndIsAvailable(String id, Boolean isAvailable);
}