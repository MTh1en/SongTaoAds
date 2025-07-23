package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Tickets;
import com.capstone.ads.model.Users;
import com.capstone.ads.model.enums.TicketSeverity;
import com.capstone.ads.model.enums.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Tickets, String> {

    Page<Tickets> findByCustomerId(String customerId, Pageable pageable);

    Page<Tickets> findByStatus(TicketStatus status, Pageable pageable);

    Page<Tickets> findBySeverity(TicketSeverity severity, Pageable pageable);

    int countClosedByCustomer(Users customer);

    int countByStatus(TicketStatus status);

}
