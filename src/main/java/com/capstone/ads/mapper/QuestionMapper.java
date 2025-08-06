package com.capstone.ads.mapper;

import com.capstone.ads.dto.question.QuestionDTO;
import com.capstone.ads.dto.question.QuestionCreateRequest;
import com.capstone.ads.dto.question.QuestionUpdateRequest;
import com.capstone.ads.model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface QuestionMapper {
    QuestionDTO toDto(Question question);

    Question mapCreateRequestToEntity(QuestionCreateRequest questionCreateRequest);

    void mapUpdateRequestToEntity(QuestionUpdateRequest questionUpdateRequest, @MappingTarget Question question);
}
