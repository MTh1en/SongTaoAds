package com.capstone.ads.dto.chatBot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FineTuningJobListResponse {
    String object;
    List<FineTuningJobResponse> data;
    @JsonProperty("has_more")
    boolean hasMore;
}