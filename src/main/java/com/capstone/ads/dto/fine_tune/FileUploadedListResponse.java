package com.capstone.ads.dto.fine_tune;

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
