package com.capstone.ads.service;

import com.capstone.ads.dto.topic.TopicDTO;
import com.capstone.ads.dto.topic.TopicRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TopicService {
    @Transactional
    TopicDTO createTopic(TopicRequest topicRequest);


    List<TopicDTO> getAllTopics();

    TopicDTO getTopicById(String id);

    @Transactional
    TopicDTO updateTopic(String id, TopicDTO topicDTO);

    void deleteTopic(String id);
}
