package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Tickets;
import com.capstone.ads.model.enums.TicketSeverity;
import com.capstone.ads.model.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Tickets, String> {
    List<Tickets> findByCustomerId(String customerId);

    List<Tickets> findByStatus(TicketStatus status);

    List<Tickets> findBySeverity(TicketSeverity severity);

}