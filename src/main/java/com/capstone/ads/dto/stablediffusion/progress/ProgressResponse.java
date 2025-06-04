package com.capstone.ads.dto.stablediffusion.progress;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProgressResponse {
    Boolean active;
    Boolean queued;
    Boolean completed;
    Double progress;
    Double eta;

    @JsonProperty("live_preview")
    String livePreview;

    @JsonProperty("id_live_preview")
    Integer idLivePreview;
    @JsonProperty("textinfo")
    String textInfo;
}
