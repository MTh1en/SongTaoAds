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
import com.capstone.ads.utils.SecurityContextUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {
    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;
    @Value("${spring.ai.openai.chat.options.model}")
    private String modelName;

    private final ChatBotLogRepository chatBotLogRepository;
    private final SecurityContextUtils securityContextUtils;
    private final ChatBotRepository chatBotRepository;
    private final ModelChatBotRepository modelChatBotRepository;
    private final ChatBotLogMapper chatBotLogMapper;
    private final ModelChatBotMapper modelChatBotMapper;


    @Override
    public String chat(ChatRequest request) {
        ChatCompletionRequest completionRequest = new ChatCompletionRequest();
        String modelChat = modelChatBotRepository.getModelChatBotByActive(true).get().getModelName();
        completionRequest.setModel(modelChat);
        completionRequest.setMessages(List.of(
                new ChatCompletionRequest.Message("system", "Bạn là trợ lý AI tư vấn về thiết kế và in ấn biển quảng cáo."),
                new ChatCompletionRequest.Message("user", request.getPrompt())
        ));
        ChatCompletionResponse response = chatBotRepository.getChatCompletions(
                "Bearer " + openaiApiKey,
                completionRequest);
        saveResponse(response, request.getPrompt());
        return response.getChoices().getFirst().getMessage().getContent();
    }

    @Override
    public String translateToTextToImagePrompt(String prompt) {
        ChatCompletionRequest completionRequest = new ChatCompletionRequest();
        completionRequest.setModel(modelName);
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
    public FileUploadResponse uploadFileToFinetune(MultipartFile file) {
        // Validate inputs
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        // Validate file type
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
        return chatBotRepository.getFineTuningJobById(
                "Bearer " + openaiApiKey
                , jobId);
    }

    public ModelChatBotDTO setModeChatBot(String jobId) {
        // Retrieve fine-tuning job details to get the model name
        FineTuningJobResponse fineTuningJob = getFineTuningJob(jobId);
        if (!"succeeded".equalsIgnoreCase(fineTuningJob.getStatus())) {
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
        String newModelName = fineTuningJob.getFineTunedModel();
        // Deactivate the current active model, if any
        modelChatBotRepository.findByActiveTrue().ifPresent(currentActiveModel -> {
            currentActiveModel.setActive(false);
            modelChatBotRepository.save(currentActiveModel);
        });

        // Create and save the new model
        ModelChatBotDTO modelChatBotDTO = new ModelChatBotDTO();
        modelChatBotDTO.setModelName(newModelName);
        modelChatBotDTO.setPreviousModelName(modelName); // Current modelName from @Value
        modelChatBotDTO.setActive(true);

        ModelChatBot modelChatBot = modelChatBotMapper.toEntity(modelChatBotDTO);
        modelChatBotRepository.save(modelChatBot);

        return modelChatBotDTO;
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
    public File uploadFileExcel(MultipartFile file, String fileName) {
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

            String userHome = System.getProperty("user.home");
            String downloadDir = userHome + File.separator + "Downloads";
            String outputFilePath = downloadDir + File.separator + (fileName.endsWith(".jsonl") ? fileName : fileName + ".jsonl");
            convertToJsonl(rowsResult, headers, outputFilePath);
            return new File(outputFilePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void convertToJsonl(List<Map<String, Object>> data, List<String> headers, String outputFilePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
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
                writer.write(jsonLine);
                writer.newLine();
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private void saveResponse(ChatCompletionResponse response, String question) {
        var userId = securityContextUtils.getCurrentUserId();
        // Extract answer from response
        String answer = (response.getChoices() != null && !response.getChoices().isEmpty())
                ? response.getChoices().getFirst().getMessage().getContent()
                : null;
        if (answer == null || answer.trim().isEmpty()) {
            throw new AppException(ErrorCode.EXTERNAL_SERVICE_ERROR);
        }
        ChatBotLog log = chatBotLogMapper.toEntity(question, answer, userId);
        chatBotLogRepository.save(log);
    }

}