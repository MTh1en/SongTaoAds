package com.capstone.ads.controller;

import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.question.QuestionCreateRequest;
import com.capstone.ads.dto.question.QuestionDTO;
import com.capstone.ads.dto.question.QuestionUpdateRequest;
import com.capstone.ads.service.QuestionService;
import com.capstone.ads.utils.ApiResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "QUESTION")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionController {
    QuestionService questionService;

    @PostMapping("/topics/{topicId}/questions")
    @Operation(summary = "Tạo câu hỏi theo topic")
    public ApiResponse<QuestionDTO> createQuestion(
            @PathVariable String topicId,
            @Valid @RequestBody QuestionCreateRequest questionCreateRequest) {
        QuestionDTO createdQuestion = questionService.createQuestion(topicId, questionCreateRequest);
        return ApiResponseBuilder.buildSuccessResponse("Tạo câu hỏi thành công", createdQuestion);
    }

    @GetMapping("/questions")
    @Operation(summary = "Xem tất cả câu hỏi")
    public ApiResponse<List<QuestionDTO>> viewAllQuestions() {
        List<QuestionDTO> questions = questionService.getAllQuestions();
        return ApiResponseBuilder.buildSuccessResponse("Xem tất cả câu hỏi than công", questions);
    }

    @GetMapping("/questions/{questionId}")
    @Operation(summary = "Xem câu hỏi theo ID")
    public ApiResponse<QuestionDTO> viewQuestionDetails(@PathVariable String questionId) {
        QuestionDTO question = questionService.findQuestionById(questionId);
        return ApiResponseBuilder.buildSuccessResponse("em câu hỏi theo ID thành công", question);
    }

    @PutMapping("/questions/{questionId}")
    @Operation(summary = "Cập nhật câu hỏi")
    public ApiResponse<QuestionDTO> updateQuestion(
            @PathVariable String questionId,
            @Valid @RequestBody QuestionUpdateRequest request) {
        QuestionDTO updatedQuestion = questionService.updateQuestion(questionId, request);
        return ApiResponseBuilder.buildSuccessResponse("Cập nhật câu hỏi thành công", updatedQuestion);
    }

    @DeleteMapping("/questions/{questionId}")
    @Operation(summary = "Xóa câu hỏi")
    public ApiResponse<Void> deleteQuestion(@PathVariable String questionId) {
        questionService.deleteQuestion(questionId);
        return ApiResponseBuilder.buildSuccessResponse("Xóa câu hỏi thành công", null);
    }

    @GetMapping("/topic/{topicId}/question")
    @Operation(summary = "Xem tất cả câu hỏi theo topic")
    public ApiResponse<List<QuestionDTO>> findQuestionsByTopicId(@PathVariable String topicId) {
        List<QuestionDTO> questions = questionService.findQuestionsByTopicId(topicId);
        return ApiResponseBuilder.buildSuccessResponse("Xem tất cả câu hỏi theo topic thành công", questions);
    }
}