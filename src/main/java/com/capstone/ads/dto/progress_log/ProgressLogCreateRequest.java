package com.capstone.ads.dto.progress_log;

import com.capstone.ads.model.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProgressLogCreateRequest {
    String orderId;
    String description;
    OrderStatus status;
    List<MultipartFile> progressLogImages;
}
