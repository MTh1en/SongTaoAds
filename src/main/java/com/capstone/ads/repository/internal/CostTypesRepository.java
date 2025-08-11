package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CostTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CostTypesRepository extends JpaRepository<CostTypes, String> {
    Optional<CostTypes> findByIdAndIsAvailable(String id, Boolean isAvailable);

    List<CostTypes> findByProductTypes_IdOrderByPriorityAsc(String id);

    Optional<CostTypes> findByProductTypes_IdAndIsCore(String id, Boolean isCore);

    boolean existsByProductTypes_IdAndIsCore(String id, Boolean isCore);
}