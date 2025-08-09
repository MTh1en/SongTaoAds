package com.capstone.ads.dto.model_chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModelResponse {
    private String id;
    private String object;
    private int created;
    @JsonProperty("owned_by")
    private String ownedBy;
}
