package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, String> {
    Optional<Payments> findByCode(Long code);
}
