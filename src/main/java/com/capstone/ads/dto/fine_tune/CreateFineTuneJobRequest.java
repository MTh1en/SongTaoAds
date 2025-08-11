package com.capstone.ads.dto.fine_tune;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateFineTuneJobRequest {
    @JsonProperty("training_file")
    String trainingFile;

    String model;

}
