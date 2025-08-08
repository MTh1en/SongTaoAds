package com.capstone.ads.service.impl;

import com.capstone.ads.constaint.S3ImageKeyFormat;
import com.capstone.ads.dto.fine_tune.FileUploadResponse;
import com.capstone.ads.dto.fine_tune.FineTuningJobResponse;
import com.capstone.ads.dto.model_chat.ModelChatBotDTO;
import com.capstone.ads.dto.webhook.FineTuneSuccess;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ModelChatBotMapper;
import com.capstone.ads.model.ModelChatBot;
import com.capstone.ads.repository.external.ChatBotClient;
import com.capstone.ads.repository.internal.ModelChatBotRepository;
import com.capstone.ads.service.FineTuneService;
import com.capstone.ads.service.ModelChatService;
import com.capstone.ads.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModelChatServiceImpl implements ModelChatService {
    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;

    private final ChatBotClient chatBotClient;
    private final ModelChatBotRepository modelChatBotRepository;
    private final S3Service s3Service;
    private final FineTuneService fineTuneService;
    private final ModelChatBotMapper modelChatBotMapper;

    @Async
    @Override
    public void createNewModelChat(FineTuneSuccess fineTuneSuccess) {
        FineTuningJobResponse fineTuningJob = fineTuneService.getFineTuningJob(fineTuneSuccess.getData().getId());
        ModelChatBot modelChatBot = ModelChatBot.builder()
                .modelName(fineTuningJob.getFineTunedModel())
                .previousModelName(fineTuningJob.getModel())
                .active(false)
                .build();
        modelChatBotRepository.save(modelChatBot);
    }

    @Override
    public Page<ModelChatBotDTO> getModelChatBots(int page, int size) {
        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        return modelChatBotRepository.findAll(pageable)
                .map(modelChatBotMapper::toDTO);
    }

    @Override
    public ModelChatBotDTO setModeChatBot(String modelChatId) {
        ModelChatBot modelChatBotActive = modelChatBotRepository.getModelChatBotByActive(true)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND));
        modelChatBotActive.setActive(false);

        ModelChatBot modelChatBot = modelChatBotRepository.findById(modelChatId)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND));
        modelChatBot.setActive(true);

        modelChatBotRepository.saveAll(new ArrayList<>(Arrays.asList(
                modelChatBotActive,
                modelChatBot
        )));
        return modelChatBotMapper.toDTO(modelChatBot);
    }

    @Override
    public ModelChatBot getModelChatBotById(String modelChatBotId) {
        return modelChatBotRepository.findById(modelChatBotId)
                .orElseThrow(() -> new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND));
    }


    @Override
    public FileUploadResponse uploadFileExcel(MultipartFile file) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            int sheetIndex = workbook.getActiveSheetIndex();
            Sheet sheetToProcess = workbook.getSheetAt(sheetIndex);
            if (sheetToProcess == null) {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }
            Iterator<Row> rows = sheetToProcess.rowIterator();
            if (!rows.hasNext()) {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }

            List<String> headers = new ArrayList<>();
            Row headerRow = rows.next();
            headerRow.forEach(cell -> headers.add(getCellValueAsString(cell)));
            if (headers.isEmpty()) {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }

            List<Map<String, Object>> rowsResult = new ArrayList<>();
            rows.forEachRemaining(row -> {
                Map<String, Object> rowMap = new LinkedHashMap<>();
                for (int i = 0; i < Math.min(headers.size(), row.getPhysicalNumberOfCells()); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowMap.put(headers.get(i), getCellValueAsString(cell));
                }
                rowsResult.add(rowMap);
            });
            if (rowsResult.isEmpty()) {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }

            String jsonlFileName = String.format(S3ImageKeyFormat.FINE_TUNE_FILE, UUID.randomUUID());

            MultipartFile jsonlFile = convertToJsonl(rowsResult, headers, jsonlFileName);
            s3Service.uploadSingleFile(jsonlFileName, jsonlFile);
            return chatBotClient.uploadFile(
                    "Bearer " + openaiApiKey,
                    "fine-tune",
                    jsonlFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MultipartFile convertToJsonl(List<Map<String, Object>> data, List<String> headers, String fileName) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            for (Map<String, Object> row : data) {
                // Transform row into OpenAI fine-tuning format (messages)
                Map<String, List<Map<String, String>>> fineTuneEntry = new HashMap<>();
                List<Map<String, String>> messages = new ArrayList<>();

                String userContent = !headers.isEmpty() ? String.valueOf(row.getOrDefault(headers.get(0), "")) : "";
                String assistantContent = headers.size() > 1 ? String.valueOf(row.getOrDefault(headers.get(1), "")) : "";

                Map<String, String> systemMessage = new HashMap<>();
                systemMessage.put("role", "system");
                systemMessage.put("content", "Bạn là trợ lý AI tư vấn về thiết kế và in ấn biển quảng cáo.");
                messages.add(systemMessage);

                Map<String, String> userMessage = new HashMap<>();
                userMessage.put("role", "user");
                userMessage.put("content", userContent);
                messages.add(userMessage);

                Map<String, String> assistantMessage = new HashMap<>();
                assistantMessage.put("role", "assistant");
                assistantMessage.put("content", assistantContent);
                messages.add(assistantMessage);

                fineTuneEntry.put("messages", messages);

                // Write each entry as a JSON line
                String jsonLine = objectMapper.writeValueAsString(fineTuneEntry);
                baos.write(jsonLine.getBytes());
                baos.write("\n".getBytes());
            }

            byte[] byteArray = baos.toByteArray();
            return new MockMultipartFile("file", fileName, "application/octet-stream", byteArray);
        } catch (IOException e) {
            log.info("Error while converting jsonl file", e);
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue().toString();
                }
                yield String.valueOf(cell.getNumericCellValue());
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
