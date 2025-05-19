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
public class OrderUpdateDTO {
     Double totalAmount;
     Double depositAmount;
    Double remainingAmount;
    String note;
     Boolean isCustomDesign;
     Timestamp deliveryDate;
    List<String> histories;
     OrderStatus status;
}
