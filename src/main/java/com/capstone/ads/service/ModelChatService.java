package com.capstone.ads.service;

import com.capstone.ads.dto.fine_tune.FileUploadResponse;
import com.capstone.ads.dto.model_chat.ModelChatBotDTO;
import com.capstone.ads.dto.webhook.FineTuneSuccess;
import com.capstone.ads.model.ModelChatBot;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ModelChatService {
    void createNewModelChat(FineTuneSuccess fineTuneSuccess);

    FileUploadResponse uploadFileExcel(MultipartFile file);

    Page<ModelChatBotDTO> getModelChatBots(int page, int size);

    ModelChatBotDTO setModeChatBot(String modelChatBotId);

    //INTERNAL FUNCTION
    ModelChatBot getModelChatBotById(String modelChatBotId);
}
