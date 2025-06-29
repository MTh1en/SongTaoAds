package com.capstone.ads.mapper;

import com.capstone.ads.dto.ticket.TicketDTO;
import com.capstone.ads.dto.ticket.TicketRequest;
import com.capstone.ads.model.Orders;
import com.capstone.ads.model.Tickets;
import com.capstone.ads.model.Users;
import com.capstone.ads.model.enums.TicketStatus;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TicketsMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(initTicketStatus())")
    Tickets sendTicket(TicketRequest request, @Context Users customer, @Context Orders orders);

    default TicketStatus initTicketStatus() {
        return TicketStatus.OPEN;
    }

    @AfterMapping
    default void afterSendTicketMapping(@MappingTarget Tickets ticket, TicketRequest request, @Context Users customer, @Context Orders orders) {
        ticket.setCustomer(customer); // Map customer to user field in Tickets
        ticket.setOrders(orders); // Map orders
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", source = "customer") // Map DTO customer to entity user
    @Mapping(target = "orders", source = "orders")
    Tickets toEntity(TicketDTO dto);

    @Mapping(target = "customer", source = "customer") // Map user from Tickets to customer in DTO
    @Mapping(target = "staff", source = "staff")  // Map staff from Tickets to staff in DTO
    @Mapping(target = "orders", source = "orders")
    TicketDTO toDTO(Tickets entity);

    @Mapping(target = "solution", source = "solution")
    @Mapping(target = "staff", source = "staff")
    @Mapping(target = "status", constant = "CLOSED")
    @AfterMapping
    default void updateSolution(@MappingTarget Tickets ticket, String solution, @Context Users staff) {
        ticket.setStaff(staff);
        ticket.setSolution(solution);
    }


}
