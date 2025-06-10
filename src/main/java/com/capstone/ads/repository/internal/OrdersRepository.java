package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Orders;
import com.capstone.ads.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, String> {
    Page<Orders> findByUsers_Id(String id, Pageable pageable);

    Page<Orders> findByStatus(OrderStatus status, Pageable pageable);

    Optional<Orders> findByIdAndStatus(String id, OrderStatus status);

    Optional<Orders> findByIdAndStatusIn(String id, Collection<OrderStatus> statuses);
}