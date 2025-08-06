package com.capstone.ads.dto.webhook;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FineTuneSuccess {
    String id;
    String type;
    Integer createAt;
    FineTuneSuccessData data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FineTuneSuccessData {
        String id;
    }
}
