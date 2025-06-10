package com.capstone.ads.mapper;

import com.capstone.ads.dto.email.TransactionalEmailRequest;
import com.capstone.ads.dto.email.transactional.Params;
import com.capstone.ads.dto.email.transactional.Recipient;
import com.capstone.ads.dto.email.transactional.Sender;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VerificationMapper {
    Sender toSender(String name, String email);

    Recipient toRecipient(String name, String email);

    Params toParams(String fullName, String url);

    TransactionalEmailRequest toTransactionalEmailRequest(Sender sender, List<Recipient> to, Params params, String subject, String htmlContent);

}
