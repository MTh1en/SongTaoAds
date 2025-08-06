package com.capstone.ads.controller;
import com.capstone.ads.dto.ApiResponse;
import com.capstone.ads.dto.question.QuestionDTO;
import com.capstone.ads.dto.question.QuestionRequest;
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
            @RequestBody QuestionRequest questionRequest) {
        QuestionDTO createdQuestion = questionService.createQuestion(topicId, questionRequest);
        return ApiResponseBuilder.buildSuccessResponse("Question created successfully", createdQuestion);
    }

    @GetMapping("/questions")
    @Operation(summary = "View all questions")
    public ApiResponse<List<QuestionDTO>> viewAllQuestions() {
        List<QuestionDTO> questions = questionService.getAllQuestions();
        return ApiResponseBuilder.buildSuccessResponse("Questions retrieved successfully", questions);
    }

    @GetMapping("/questions/{id}")
    @Operation(summary = "View question details")
    public ApiResponse<QuestionDTO> viewQuestionDetails(@PathVariable String id) {
        QuestionDTO question = questionService.getQuestionById(id);
        return ApiResponseBuilder.buildSuccessResponse("Question details retrieved successfully", question);
    }

    @PutMapping("/questions/{id}")
    @Operation(summary = "Update a question")
    public ApiResponse<QuestionDTO> updateQuestion(
            @PathVariable String id,
            @Valid @RequestBody QuestionDTO questionDTO) {
        QuestionDTO updatedQuestion = questionService.updateQuestion(id, questionDTO);
        return ApiResponseBuilder.buildSuccessResponse("Question updated successfully", updatedQuestion);
    }

    @DeleteMapping("/questions/{id}")
    @Operation(summary = "Delete a question")
    public ApiResponse<Void> deleteQuestion(@PathVariable String id) {
        questionService.deleteQuestion(id);
        return ApiResponseBuilder.buildSuccessResponse("Question deleted successfully", null);
    }

    @GetMapping("/topic/{topicId}/question")
    @Operation(summary = "View all questions under a topic")
    public ApiResponse<List<QuestionDTO>> viewQuestionsByTopicId(@PathVariable String topicId) {
        List<QuestionDTO> questions = questionService.getQuestionsByTopicId(topicId);
        return ApiResponseBuilder.buildSuccessResponse("Questions retrieved successfully", questions);
    }
}