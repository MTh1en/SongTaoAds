package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Orders;
import com.capstone.ads.model.Users;
import com.capstone.ads.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, String> {
    Page<Orders> findByUsers_Id(String id, Pageable pageable);

    Page<Orders> findByStatus(OrderStatus status, Pageable pageable);
  
    int countByUsers_IdAndStatus(String id, OrderStatus status);

    int countByUsers(Users users);

    int countByStatus(OrderStatus status);

}