package com.capstone.ads.dto.stablediffusion.progress;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProgressRequest {
    @JsonProperty("id_task")
    String idTask;

    @Builder.Default
    @JsonProperty("id_live_preview")
    Integer idLivePreview = -1;

    @Builder.Default
    @JsonProperty("live_preview")
    Boolean livePreview = true;
}
