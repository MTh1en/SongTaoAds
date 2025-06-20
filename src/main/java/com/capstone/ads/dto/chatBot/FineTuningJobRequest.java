package com.capstone.ads.dto.chatBot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FineTuningJobRequest {
    @JsonProperty("training_file")
     String trainingFile;

     String model;

}
