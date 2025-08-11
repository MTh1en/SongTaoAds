package com.capstone.ads.dto.fine_tune;

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

    @JsonProperty("method")
    private Method method;

    @Data
    @Builder
    public static class Method {
        @JsonProperty("type")
        String type;

        @JsonProperty("supervised")
        Supervised supervised;

        @Data
        @Builder
        public static class Supervised {
            @JsonProperty("hyperparameters")
            Hyperparameters hyperparameters;


            @Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            @FieldDefaults(level = AccessLevel.PRIVATE)
            public static class Hyperparameters {
                @JsonProperty("n_epochs")
                Integer nEpochs;

                @JsonProperty("batch_size")
                Integer batchSize;

                @JsonProperty("learning_rate_multiplier")
                Double learningRateMultiplier;

            }
        }
    }
}
