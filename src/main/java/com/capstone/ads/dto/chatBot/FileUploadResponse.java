package com.capstone.ads.dto.chatBot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
