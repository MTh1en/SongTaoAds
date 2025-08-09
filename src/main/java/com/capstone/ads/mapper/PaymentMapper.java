package com.capstone.ads.mapper;

import com.capstone.ads.dto.payment.PaymentDTO;
import com.capstone.ads.model.Payments;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDTO toDTO(Payments payments);
}
