package com.capstone.ads.dto.edited_design;

import com.capstone.ads.dto.CoreDTO;
import com.capstone.ads.dto.customer_detail.CustomerDetailDTO;
import com.capstone.ads.dto.design_template.DesignTemplateDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditedDesignDTO {
    String id;
    String editedImage;
    String customerNote;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    CustomerDetailDTO customerDetail;
    CoreDTO designTemplates;
    CoreDTO backgrounds;
}
