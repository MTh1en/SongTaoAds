package com.capstone.ads.dto.stable_diffusion.pendingtask;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PendingTaskResponse {
    Double size;
    List<String> tasks;
}
