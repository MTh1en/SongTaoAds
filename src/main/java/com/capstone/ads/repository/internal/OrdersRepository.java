package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, String> {
    List<Orders> findByUsers_Id(String id);

}