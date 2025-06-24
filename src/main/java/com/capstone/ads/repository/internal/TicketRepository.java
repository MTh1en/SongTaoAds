package com.capstone.ads.repository.internal;

import com.capstone.ads.model.Tickets;
import com.capstone.ads.model.enums.TicketSeverity;
import com.capstone.ads.model.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Tickets, String> {
    List<Tickets> findByStatus(TicketStatus status);

    List<Tickets> findBySeverity(TicketSeverity severity);

}