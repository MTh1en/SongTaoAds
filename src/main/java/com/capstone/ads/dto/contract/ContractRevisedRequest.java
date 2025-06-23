package com.capstone.ads.dto.contract;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractRevisedRequest {
    @Range(min = 1, max = 100, message = "Percent between from 1 to 100")
    Long depositPercentChanged;
    @NotNull(message = "Contract File is Required")
    MultipartFile contactFile;
}
