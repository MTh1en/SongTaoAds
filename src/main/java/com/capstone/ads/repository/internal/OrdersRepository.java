package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Orders;
import com.capstone.ads.model.Users;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.model.enums.OrderType;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, String> {
    Page<Orders> findByUsers_Id(String id, Pageable pageable);

    Orders findByOrderCode(String orderCode);

    Page<Orders> findByStatus(OrderStatus status, Pageable pageable);

    Page<Orders> findByOrderType(OrderType orderType, Pageable pageable);

    Page<Orders> findByStatusAndOrderType(OrderStatus status, OrderType orderType, Pageable pageable);

    Page<Orders> findByStatusAndOrderTypeIn(OrderStatus status, List<OrderType> orderTypes, Pageable pageable);

    Page<Orders> findByOrderTypeIn(List<OrderType> orderTypes, Pageable pageable);

    @EntityGraph(attributePaths = {
            "orderDetails",
            "orderDetails.customDesignRequests",
    })
    @NonNull
    Optional<Orders> findById(@NonNull String id);

    int countByStatus(OrderStatus status);

    int countByStatusNotIn(Collection<OrderStatus> statuses);

    int countByOrderTypeIn(Collection<OrderType> orderTypes);

    int countByUpdatedAtBetween(LocalDateTime updatedAtStart, LocalDateTime updatedAtEnd);

    int countByStatusAndUpdatedAtBetween(OrderStatus status, LocalDateTime updatedAtStart, LocalDateTime updatedAtEnd);

    @Query("""
            select o from Orders o
            where (o.orderCode like concat('%', :orderCode, '%')
            or o.users.fullName like concat('%', :fullName, '%')
            or o.users.phone like concat('%', :phone, '%'))
            and o.orderType in :orderTypes""")
    Page<Orders> searchSaleOrder(@Param("orderCode") String orderCode,
                                 @Param("fullName") String fullName,
                                 @Param("phone") String phone,
                                 @Param("orderTypes") Collection<OrderType> orderTypes,
                                 Pageable pageable);

    Page<Orders> findByOrderCodeContainsIgnoreCaseAndUsers(String orderCode, Users users, Pageable pageable);
}