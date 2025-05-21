package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerDetailRepository extends JpaRepository<CustomerDetail, String> {
    @Query("select c from CustomerDetail c where c.users.id = ?1")
    Optional<CustomerDetail> findByUsers_Id(String id);

    boolean existsByLogoUrl(String logoUrl);

}