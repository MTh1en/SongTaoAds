package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerDetailRepository extends JpaRepository<CustomerDetail, String> {
    Optional<CustomerDetail> findByUsers_Id(String id);

    boolean existsByLogoUrl(String logoUrl);

}