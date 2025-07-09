package com.capstone.ads.mapper;

import com.capstone.ads.dto.email.TransactionalEmailRequest;
import com.capstone.ads.dto.email.transactional.Recipient;
import com.capstone.ads.dto.email.transactional.Sender;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VerificationMapper {
    Sender toSender(String name, String email);

    Recipient toRecipient(String email);

    TransactionalEmailRequest toTransactionalEmailRequest(Sender sender, List<Recipient> to, String subject, String htmlContent);
}
