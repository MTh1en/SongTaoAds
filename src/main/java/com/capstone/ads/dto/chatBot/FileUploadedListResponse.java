package com.capstone.ads.dto.chatBot;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileUploadedListResponse {
    String object;
    List<FileUploadResponse> data;
}
