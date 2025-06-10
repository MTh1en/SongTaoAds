package com.capstone.ads.repository.external;

import com.capstone.ads.dto.email.TransactionalEmailRequest;
import com.capstone.ads.dto.email.TransactionalEmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "brevo", url = "https://api.brevo.com")
public interface BrevoRepository {
    @PostMapping(value = "/v3/smtp/email", produces = MediaType.APPLICATION_JSON_VALUE)
    TransactionalEmailResponse sendTransactionalEmail(@RequestHeader("api-key") String apiKey,
                                                      @RequestBody TransactionalEmailRequest body);
}
