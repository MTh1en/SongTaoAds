package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Payments;
import com.capstone.ads.model.enums.PaymentMethod;
import com.capstone.ads.model.enums.PaymentStatus;
import com.capstone.ads.model.enums.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, String> {
    Optional<Payments> findByCode(Long code);

    Page<Payments> findByOrders_Id(String id, Pageable pageable);

    List<Payments> findByOrders_OrderCode(String orderCode);

    Page<Payments> findByOrders_Users_Id(String id, Pageable pageable);

    List<Payments> findByStatusAndMethod(PaymentStatus status, PaymentMethod method);

    List<Payments> findByStatus(PaymentStatus status);

    List<Payments> findByTypeInAndStatus(Collection<PaymentType> types, PaymentStatus status);

    int countByStatus(PaymentStatus status);

    List<Payments> findByStatusAndUpdatedAtBetween(PaymentStatus status, LocalDateTime updatedAtStart, LocalDateTime updatedAtEnd);

    List<Payments> findByStatusAndMethodAndUpdatedAtBetween(PaymentStatus status, PaymentMethod method, LocalDateTime updatedAtStart, LocalDateTime updatedAtEnd);

    List<Payments> findByTypeInAndStatusAndUpdatedAtBetween(Collection<PaymentType> types, PaymentStatus status, LocalDateTime updatedAtStart, LocalDateTime updatedAtEnd);
}
