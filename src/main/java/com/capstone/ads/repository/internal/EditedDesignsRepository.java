package com.capstone.ads.repository.internal;

import com.capstone.ads.model.EditedDesigns;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditedDesignsRepository extends JpaRepository<EditedDesigns, String> {
    Page<EditedDesigns> findByCustomerDetail_Id(String id, Pageable pageable);
}