package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerDetail;
import com.capstone.ads.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerDetailRepository extends JpaRepository<CustomerDetail, String> {
    boolean existsByLogoUrl(String logoUrl);
    @Query("SELECT o FROM CustomerDetail o WHERE o.id = :userId")
    Optional<CustomerDetail> getCustomerDetailByUserId(@Param("userId") String userId);
}