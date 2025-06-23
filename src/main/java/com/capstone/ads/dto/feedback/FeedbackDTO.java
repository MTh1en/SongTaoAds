package com.capstone.ads.dto.feedback;

import com.capstone.ads.dto.order.OrderDTO;
import com.capstone.ads.dto.user.UserDTO;
import com.capstone.ads.model.enums.FeedbackStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackDTO {
    String id;
    Integer rating;
    String comment;
    String feedbackImageUrl;
    LocalDateTime sendAt;
    String response;
    LocalDateTime responseAt;
    FeedbackStatus status;
    UserDTO sendBy;
    String orderId;
}
