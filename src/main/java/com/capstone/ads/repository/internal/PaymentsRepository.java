package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface PaymentsRepository extends JpaRepository<Payments, String> {
    @Query("SELECT p FROM Payments p WHERE p.orders.id = :ordersId")
    Optional<Payments> findOrdersId(@Param("ordersId") String ordersId);
}
