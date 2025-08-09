package com.capstone.ads.dto.fine_tune;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileUploadResponse {
      String id;
      String object;
      String filename;
      String purpose;

      @JsonProperty("created_at")
      Integer createdAt;
      Integer bytes;
}
