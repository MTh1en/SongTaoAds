package com.capstone.ads.service.impl;

import com.capstone.ads.dto.chatBot.*;
import com.capstone.ads.exception.AppException;
import com.capstone.ads.exception.ErrorCode;
import com.capstone.ads.mapper.ChatBotLogMapper;
import com.capstone.ads.mapper.ModelChatBotMapper;
import com.capstone.ads.model.ChatBotLog;
import com.capstone.ads.model.ModelChatBot;
import com.capstone.ads.repository.external.ChatBotRepository;
import com.capstone.ads.repository.internal.ChatBotLogRepository;
import com.capstone.ads.repository.internal.ModelChatBotRepository;
import com.capstone.ads.service.ChatBotService;
import com.capstone.ads.service.S3Service;
import com.capstone.ads.utils.SecurityContextUtils;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatBotServiceImpl implements ChatBotService {
    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;

    private final ChatBotLogRepository chatBotLogRepository;
    private final SecurityContextUtils securityContextUtils;
    private final ChatBotRepository chatBotRepository;
    private final ModelChatBotRepository modelChatBotRepository;
    private final S3Service s3Service;
    private final ChatBotLogMapper chatBotLogMapper;
    private final ModelChatBotMapper modelChatBotMapper;


    @Override
    public String chat(ChatRequest request) {
        ChatCompletionRequest completionRequest = new ChatCompletionRequest();
        ModelChatBot modelChatBot = modelChatBotRepository.getModelChatBotByActive(true)
                .orElseThrow(()->new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND));
        String modelChat = modelChatBot.getModelName();
        completionRequest.setModel(modelChat);
        completionRequest.setMessages(List.of(
                new ChatCompletionRequest.Message("system", "Bạn là trợ lý AI tư vấn về thiết kế và in ấn biển quảng cáo."),
                new ChatCompletionRequest.Message("user", request.getPrompt())
        ));
        ChatCompletionResponse response = chatBotRepository.getChatCompletions(
                "Bearer " + openaiApiKey,
                completionRequest);
        saveResponse(response, request.getPrompt(), modelChatBot);
        return response.getChoices().getFirst().getMessage().getContent();
    }

    @Override
    public String TestChat(TestChatRequest request) {
        ChatCompletionRequest completionRequest = new ChatCompletionRequest();
        completionRequest.setModel(request.getModel());
        completionRequest.setMessages(List.of(
                new ChatCompletionRequest.Message("system", "Bạn là trợ lý AI tư vấn về thiết kế và in ấn biển quảng cáo."),
                new ChatCompletionRequest.Message("user", request.getPrompt())
        ));
        ChatCompletionResponse response = chatBotRepository.getChatCompletions(
                "Bearer " + openaiApiKey,
                completionRequest);
        return response.getChoices().getFirst().getMessage().getContent();
    }

    @Override
    public String translateToTextToImagePrompt(String prompt) {
        ChatCompletionRequest completionRequest = new ChatCompletionRequest();
        ModelChatBot modelChatBot = modelChatBotRepository.getModelChatBotByActive(true)
                .orElseThrow(()->new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND));
        completionRequest.setModel(modelChatBot.getModelName());
        completionRequest.setMessages(List.of(
                new ChatCompletionRequest.Message("system", "Based on the customer's request, let write a prompt in English to createAttribute an image of the billboard. Note: Do not createAttribute an image, only the prompt in English in the answer in one line and no prefix like the answer is: ..."),
                new ChatCompletionRequest.Message("user", "The customer request is:" + prompt)
        ));
        ChatCompletionResponse response = chatBotRepository.getChatCompletions(
                "Bearer " + openaiApiKey,
                completionRequest);
        return response.getChoices().getFirst().getMessage().getContent();
    }

    @Override
    public FileUploadResponse uploadFileToFineTune(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".jsonl")) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        // Call OpenAI file upload API
        return chatBotRepository.uploadFile(
                "Bearer " + openaiApiKey,
                "fine-tune",
                file);
    }

    @Override
    public FineTuningJobResponse fineTuningJob(FineTuningJobRequest request) {
        // Call OpenAI fine-tuning API
        FineTuningJobResponse response;
        response = chatBotRepository.createFineTuningJob(
                "Bearer " + openaiApiKey,
                request);
        return response;
    }

    @Override
    public FileUploadResponse getUploadedFileById(String fileId) {
        if (fileId == null || fileId.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        return chatBotRepository.getFileById(
                "Bearer " + openaiApiKey,
                fileId);
    }

    @Override
    public List<FileUploadResponse> getAllUploadedFiles() {
        FileUploadedListResponse response = chatBotRepository.getFiles(
                "Bearer " + openaiApiKey);
        return response.getData();
    }

    @Override
    public FileDeletionResponse deleteUploadedFile(String fileId) {
        if (fileId == null || fileId.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        return chatBotRepository.deleteFileById(
                "Bearer " + openaiApiKey,
                fileId);
    }

    @Override
    public List<FineTuningJobResponse> getAllFineTuneJobs() {
        FineTuningJobListResponse response = chatBotRepository.getFineTuningJobs(
                "Bearer " + openaiApiKey);
        return response.getData();
    }

    @Override
    public FineTuningJobResponse getFineTuningJob(String jobId) {
        FineTuningJobResponse response =chatBotRepository.getFineTuningJobById(
                "Bearer " + openaiApiKey
                , jobId);
        if ("succeeded".equalsIgnoreCase(response.getStatus())) {
            String newModelName = response.getFineTunedModel();
            ModelChatBotDTO modelChatBotDTO = new ModelChatBotDTO();
            modelChatBotDTO.setModelName(newModelName);
            modelChatBotDTO.setPreviousModelName(response.getModel());
            modelChatBotDTO.setActive(false);

            ModelChatBot modelChatBot = modelChatBotMapper.toEntity(modelChatBotDTO);
            modelChatBotRepository.save(modelChatBot);
        }
        return response;
    }

    @Override
    public ListModelsResponse getModels(){
        return chatBotRepository.getModels(
                "Bearer " + openaiApiKey
        );
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
        ModelChatBot modelChatBotActive= modelChatBotRepository.getModelChatBotByActive(true)
                .orElseThrow(()->new AppException(ErrorCode.MODEL_CHAT_NOT_FOUND));
        modelChatBotActive.setActive(false);
        ModelChatBot modelChatBot= modelChatBotRepository.getById(modelChatId);
        modelChatBot.setActive(true);
        return modelChatBotMapper.toDTO(modelChatBot);
    }


    @Override
    public FineTuningJobResponse cancelFineTuningJob(String fineTuningJobId) {

        if (fineTuningJobId == null || fineTuningJobId.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        return chatBotRepository.cancelFineTuningJob(
                "Bearer " + openaiApiKey,
                fineTuningJobId);
    }



        @Override
        public FileUploadResponse uploadFileExcel(MultipartFile file, String fileName) {
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
                String jsonlFileName = fileName.endsWith(".jsonl") ? fileName : fileName + ".jsonl";
                MultipartFile jsonlFile = convertToJsonl(rowsResult, headers, jsonlFileName);
                String s3Key = String.format("uploadJsonlFile/%s_%s", UUID.randomUUID(), jsonlFileName);
                String awsLink = s3Service.uploadSingleFile(s3Key, jsonlFile);
                return chatBotRepository.uploadFile(
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
                return new MockMultipartFile(fileName, byteArray);
            } catch (IOException e) {
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


    private void saveResponse(ChatCompletionResponse response, String question, ModelChatBot modelChat) {
        var user = securityContextUtils.getCurrentUser();
        // Extract answer from response
        String answer = (response.getChoices() != null && !response.getChoices().isEmpty())
                ? response.getChoices().getFirst().getMessage().getContent()
                : null;
        if (answer == null || answer.trim().isEmpty()) {
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
        ChatBotLog log = chatBotLogMapper.toEntity(question, answer);
        log.setModelChatBot(modelChat);
        log.setUsers(user);
        chatBotLogRepository.save(log);
    }

}