package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Feedbacks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbacksRepository extends JpaRepository<Feedbacks, String> {
    Page<Feedbacks> findBySendBy_Id(String id, Pageable pageable);

    List<Feedbacks> findByOrders_Id(String id);

    List<Feedbacks> findByOrders_OrderCode(String orderCode);
}