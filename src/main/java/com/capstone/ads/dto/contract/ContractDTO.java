package com.capstone.ads.dto.contract;

import com.capstone.ads.model.enums.ContractStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractDTO {
    String id;
    String contractNumber;
    String contractUrl;
    String signedContractUrl;
    LocalDateTime sentDate;
    LocalDateTime signedDate;
    Long depositPercentChanged;
    ContractStatus status;
}
