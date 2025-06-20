package com.capstone.ads.model;

import com.capstone.ads.model.enums.ContractStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String contractNumber;
    String contractUrl;
    String signedContractUrl;
    LocalDateTime sentDate;
    LocalDateTime signedDate;
    String note;

    @Enumerated(EnumType.STRING)
    ContractStatus status;

    @OneToOne
    Orders orders;
}
