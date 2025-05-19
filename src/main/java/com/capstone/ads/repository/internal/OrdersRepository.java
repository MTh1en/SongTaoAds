package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, String> {
    boolean existsByAiDesignsId(String aiDesignId);
}