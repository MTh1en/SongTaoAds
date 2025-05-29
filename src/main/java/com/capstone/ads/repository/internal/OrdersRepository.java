package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Orders;
import com.capstone.ads.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, String> {
    List<Orders> findByUsers_Id(String id);

    List<Orders> findByStatus(OrderStatus status);

    Optional<Orders> findByIdAndStatus(String id, OrderStatus status);

    Optional<Orders> findByIdAndStatusIn(String id, Collection<OrderStatus> statuses);
}