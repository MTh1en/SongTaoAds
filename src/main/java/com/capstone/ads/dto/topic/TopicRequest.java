package com.capstone.ads.dto.topic;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopicRequest {
    String title;
    String description;
    int maxQuestion;

}
