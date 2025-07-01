package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Sizes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SizesRepository extends JpaRepository<Sizes, String> {
    Optional<Sizes> findByIdAndIsAvailable(String id, Boolean isAvailable);
}