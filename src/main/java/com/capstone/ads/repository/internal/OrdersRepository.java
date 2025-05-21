package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, String> {

    @Query("SELECT o FROM Orders o WHERE o.id = :userId")
    Optional<Orders> getOrderByUserId(@Param("userId") String userId);
}