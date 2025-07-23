package com.capstone.ads.repository.internal;

import com.capstone.ads.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, String> {
    Optional<OrderDetails> findByCustomDesignRequests_Id(String id);

    Optional<OrderDetails> findByEditedDesigns_Id(String id);
}