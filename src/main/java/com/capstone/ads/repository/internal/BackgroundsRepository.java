package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Backgrounds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BackgroundsRepository extends JpaRepository<Backgrounds, String> {
    Optional<Backgrounds> findByIdAndIsAvailable(String id, Boolean isAvailable);

    Page<Backgrounds> findByAttributeValues_IdAndIsAvailable(String id, Boolean isAvailable, Pageable pageable);

    List<Backgrounds> findByAttributeValues_IdInAndIsAvailable(Set<String> ids, Boolean isAvailable);

    int countByIsAvailable(Boolean isAvailable);
}