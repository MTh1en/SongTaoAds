package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDetailRepository extends JpaRepository<CustomerDetail, String> {
}