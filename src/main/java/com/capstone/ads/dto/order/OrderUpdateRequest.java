package com.capstone.ads.dto.order;
import com.capstone.ads.model.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.sql.Timestamp;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUpdateRequest {
     Double totalAmount;
     Double depositAmount;
     Double remainingAmount;
     String address;
     String note;
     Boolean isCustomDesign;
     Timestamp deliveryDate;
     List<String> histories;
     OrderStatus status;
}
