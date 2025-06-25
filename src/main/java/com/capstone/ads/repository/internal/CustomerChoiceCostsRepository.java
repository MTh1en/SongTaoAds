package com.capstone.ads.repository.internal;

import com.capstone.ads.model.CustomerChoiceCosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerChoiceCostsRepository extends JpaRepository<CustomerChoiceCosts, String> {
}