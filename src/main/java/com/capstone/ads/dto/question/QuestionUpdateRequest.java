package com.capstone.ads.dto.question;

import com.capstone.ads.dto.CoreDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionUpdateRequest {
    @NotBlank(message = "Question is Required")
    String question;
}
