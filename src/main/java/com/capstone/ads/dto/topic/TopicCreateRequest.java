package com.capstone.ads.dto.topic;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopicCreateRequest {
    @NotBlank(message = "Title is Required")
    String title;
    String description;
}
