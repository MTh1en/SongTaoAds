package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Payments;
import com.capstone.ads.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, String> {
    Optional<Payments> findByCode(Long code);

    long countByOrders_Users_Id(String id);

    @Query("SELECT SUM(p.amount) FROM Payments p WHERE p.status= 'SUCCESS'")
    long sumAmount();

    @Query("SELECT SUM(p.amount) FROM Payments p WHERE p.type = 'DEPOSIT_DESIGN'")
    long sumDepositAmount();
}
