package com.capstone.ads.mapper;

import com.capstone.ads.dto.ticket.TicketDTO;
import com.capstone.ads.dto.ticket.TicketRequest;
import com.capstone.ads.model.Orders;
import com.capstone.ads.model.Tickets;
import com.capstone.ads.model.Users;
import com.capstone.ads.model.enums.OrderStatus;
import com.capstone.ads.model.enums.TicketStatus;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TicketsMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(initTicketStatus())")
    Tickets sendTicket(TicketRequest request, @Context Users user, @Context Orders orders);

    default TicketStatus initTicketStatus() {
        return TicketStatus.OPEN;
    }

    @AfterMapping
    default void afterSendTicketMapping(@MappingTarget Tickets ticket, TicketRequest request, @Context Users user, @Context Orders orders) {
        ticket.setUser(user); // Map user to staff
        ticket.setOrders(orders); // Map orders
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "orders", source = "orders")
    Tickets toEntity(TicketDTO dto);

    @Mapping(target = "user", source = "user") // Assuming staffId in TicketDTO
    @Mapping(target = "orders", source = "orders") // Assuming orderId in TicketDTO
    TicketDTO toDTO(Tickets entity);

    @Mapping(target = "solution", source = "solution")
    @Mapping(target = "status", constant = "CLOSED")
    void updateSolution(@MappingTarget Tickets ticket, String solution, @Context Users currentUser);

    @AfterMapping
    default void afterUpdateSolution(@MappingTarget Tickets ticket, @Context Users currentUser) {
        ticket.setStaff(currentUser); // Update staff to current user
    }
}
