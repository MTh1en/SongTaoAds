package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Payments;
import com.capstone.ads.model.enums.PaymentMethod;
import com.capstone.ads.model.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, String> {
    Optional<Payments> findByCode(Long code);

    long countByOrders_Users_Id(String id);

    @Query("SELECT SUM(p.amount) FROM Payments p WHERE p.status= 'SUCCESS'")
    long sumAmount();

    @Query("SELECT SUM(p.amount) FROM Payments p WHERE p.type = 'DEPOSIT_DESIGN'")
    long sumDepositAmount();

    Page<Payments> findByOrders_Id(String id, Pageable pageable);

    Page<Payments> findByOrders_Users_Id(String id, Pageable pageable);

    List<Payments> findByStatus(PaymentStatus status);

    List<Payments> findByStatusAndMethod(PaymentStatus status, PaymentMethod method);
}
