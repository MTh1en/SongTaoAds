package com.capstone.ads.repository.internal;

import com.capstone.ads.model.AIDesigns;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIDesignsRepository extends JpaRepository<AIDesigns, String> {
    Page<AIDesigns> findByCustomerDetail_Id(String id, Pageable pageable);
}