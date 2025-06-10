package com.capstone.ads.dto.email;

import com.capstone.ads.dto.email.transactional.Params;
import com.capstone.ads.dto.email.transactional.Recipient;
import com.capstone.ads.dto.email.transactional.Sender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionalEmailRequest {
    Sender sender;
    List<Recipient> to;
    Params params;
    String subject;
    String htmlContent;
}
