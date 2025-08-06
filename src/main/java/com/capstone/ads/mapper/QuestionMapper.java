package com.capstone.ads.mapper;

import com.capstone.ads.dto.question.QuestionDTO;
import com.capstone.ads.dto.question.QuestionRequest;
import com.capstone.ads.model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "topicId", source = "topic.id")
    QuestionDTO toDto(Question question);

    Question toEntity(QuestionDTO questionDTO);


    Question createQuestion( QuestionRequest questionRequest);

}
