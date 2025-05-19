package com.capstone.ads.dto.customerDetail;

import com.capstone.ads.model.Users;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDetailDTO {
    private String id;
    private String logoUrl;
    private String companyName;
    private String tagLine;
    private String contactInfo;
    private String userId;
}
