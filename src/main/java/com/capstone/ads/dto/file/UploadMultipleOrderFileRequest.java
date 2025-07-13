package com.capstone.ads.dto.file;

import com.capstone.ads.model.enums.FileTypeEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadMultipleOrderFileRequest {
    FileTypeEnum fileType;
    List<MultipartFile> files;
}
