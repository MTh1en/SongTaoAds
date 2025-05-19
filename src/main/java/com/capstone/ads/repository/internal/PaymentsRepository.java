package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentsRepository extends JpaRepository<Payments, String> {
    Optional<Payments> findByIdAndOrdersId(String id, String ordersId);
}
