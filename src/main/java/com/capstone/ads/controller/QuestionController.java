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
    @Operation(summary = "Create a new question under a topic")
    public ApiResponse<QuestionDTO> createQuestion(
            @PathVariable String topicId,
            @Valid @RequestBody QuestionCreateRequest questionCreateRequest) {
        QuestionDTO createdQuestion = questionService.createQuestion(topicId, questionCreateRequest);
        return ApiResponseBuilder.buildSuccessResponse("Question created successfully", createdQuestion);
    }

    @GetMapping("/questions")
    @Operation(summary = "View all questions")
    public ApiResponse<List<QuestionDTO>> viewAllQuestions() {
        List<QuestionDTO> questions = questionService.getAllQuestions();
        return ApiResponseBuilder.buildSuccessResponse("Questions retrieved successfully", questions);
    }

    @GetMapping("/questions/{questionId}")
    @Operation(summary = "View question details")
    public ApiResponse<QuestionDTO> viewQuestionDetails(@PathVariable String questionId) {
        QuestionDTO question = questionService.findQuestionById(questionId);
        return ApiResponseBuilder.buildSuccessResponse("Question details retrieved successfully", question);
    }

    @PutMapping("/questions/{questionId}")
    @Operation(summary = "Update a question")
    public ApiResponse<QuestionDTO> updateQuestion(
            @PathVariable String questionId,
            @Valid @RequestBody QuestionUpdateRequest request) {
        QuestionDTO updatedQuestion = questionService.updateQuestion(questionId, request);
        return ApiResponseBuilder.buildSuccessResponse("Question updated successfully", updatedQuestion);
    }

    @DeleteMapping("/questions/{questionId}")
    @Operation(summary = "Delete a question")
    public ApiResponse<Void> deleteQuestion(@PathVariable String questionId) {
        questionService.deleteQuestion(questionId);
        return ApiResponseBuilder.buildSuccessResponse("Question deleted successfully", null);
    }

    @GetMapping("/topic/{topicId}/question")
    @Operation(summary = "View all questions under a topic")
    public ApiResponse<List<QuestionDTO>> viewQuestionsByTopicId(@PathVariable String topicId) {
        List<QuestionDTO> questions = questionService.getQuestionsByTopicId(topicId);
        return ApiResponseBuilder.buildSuccessResponse("Questions retrieved successfully", questions);
    }
}