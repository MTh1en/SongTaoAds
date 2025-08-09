package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Orders;
import com.capstone.ads.model.Users;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.model.enums.OrderType;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, String> {
    Page<Orders> findByUsers_Id(String id, Pageable pageable);

    Orders findByOrderCode(String orderCode);

    Page<Orders> findByStatus(OrderStatus status, Pageable pageable);

    Page<Orders> findByOrderType(OrderType orderType, Pageable pageable);

    Page<Orders> findByStatusAndOrderType(OrderStatus status, OrderType orderType, Pageable pageable);

    int countByUsers_IdAndStatus(String id, OrderStatus status);

    int countByUsers(Users users);

    int countByStatus(OrderStatus status);

    @EntityGraph(attributePaths = {
            "orderDetails",
            "orderDetails.customDesignRequests",
    })
    @NonNull
    Optional<Orders> findById(@NonNull String id);

}