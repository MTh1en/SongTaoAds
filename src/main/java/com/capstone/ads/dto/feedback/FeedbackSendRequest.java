package com.capstone.ads.dto.feedback;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeedbackSendRequest {
    @Range(min = 1, max = 5, message = "rating between 1 to 5")
    Integer rating;
    @NotBlank(message = "Comment is Required")
    String comment;
}
