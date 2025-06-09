package com.capstone.ads.dto.email.transactional;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Params {
    @JsonProperty("FULLNAME")
    String fullName;
    @JsonProperty("URL")
    String url;
}
