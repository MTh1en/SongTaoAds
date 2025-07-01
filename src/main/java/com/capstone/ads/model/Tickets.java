package com.capstone.ads.model;

import com.capstone.ads.model.enums.TicketSeverity;
import com.capstone.ads.model.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Tickets {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
     String id;
     String title;
     String description;
     TicketSeverity severity;
     String solution;
     TicketStatus status; // ticket status: Open, In Progress, Closed

     @ManyToOne
     Users customer;

     @ManyToOne
     Users staff;

     @ManyToOne
     Orders orders;

     @CreationTimestamp
     @Column(updatable = false)
     LocalDateTime createdAt;

     @CreationTimestamp
     @Column(updatable = true)
     LocalDateTime updatedAt;
}