package com.capstone.ads.dto.chatBot;

import com.capstone.ads.dto.model_chat.ModelResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListModelsResponse {
    String object;
    List<ModelResponse> data;
}
