package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<Payments, String> {
}