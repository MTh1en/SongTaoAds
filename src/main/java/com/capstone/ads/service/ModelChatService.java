package com.capstone.ads.service;

import com.capstone.ads.dto.chatBot.FileDeletionResponse;
import com.capstone.ads.dto.chatBot.FileUploadResponse;
import com.capstone.ads.dto.chatBot.ListModelsResponse;
import com.capstone.ads.dto.chatBot.ModelChatBotDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ModelChatService { ;
    FileUploadResponse uploadFileExcel(MultipartFile file, String fileName);


    Page<ModelChatBotDTO> getModelChatBots(int page, int size);

    ModelChatBotDTO setModeChatBot(String modelChatBotId);
}
