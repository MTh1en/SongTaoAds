package com.capstone.ads.dto.chatBot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.boot.Metadata;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FineTuningJobResponse {
     String object;
     String id;
     String model;

    @JsonProperty("created_at")
       Integer createdAt;

    @JsonProperty("fine_tuned_model")
     String fineTunedModel;

    @JsonProperty("organization_id")
     String organizationId;

    @JsonProperty("result_files")
     List<String> resultFiles;

     String status;

    @JsonProperty("validation_file")
     String validationFile;

    @JsonProperty("training_file")
     String trainingFile;

     Method method;



    @JsonProperty("finished_at")
        Integer finishedAt;

     Method.Dpo.Hyperparameters hyperparameters;
        Integer seed;

    @JsonProperty("estimated_finish")
        Integer estimatedFinish;

     List<Object>   Integeregrations;

    @Data
    public static class Method {
         String type;
         Dpo dpo;

        @Data
        public static class Dpo {
             Hyperparameters hyperparameters;

            @Data
            public static class Hyperparameters {
                 double beta;

                @JsonProperty("batch_size")
                 String batchSize;

                @JsonProperty("learning_rate_multiplier")
                 String learningRateMultiplier;

                @JsonProperty("n_epochs")
                 String nEpochs;
            }
        }
    }

}
