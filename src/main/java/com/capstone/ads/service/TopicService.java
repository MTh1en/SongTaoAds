package com.capstone.ads.service;

import com.capstone.ads.dto.topic.TopicDTO;
import com.capstone.ads.dto.topic.TopicCreateRequest;
import com.capstone.ads.dto.topic.TopicUpdateInformationRequest;
import com.capstone.ads.model.Topic;

import java.util.List;

public interface TopicService {
    TopicDTO createTopic(TopicCreateRequest request);

    List<TopicDTO> findAllTopics();

    TopicDTO findTopicById(String id);

    TopicDTO updateTopic(String id, TopicUpdateInformationRequest request);

    void deleteTopic(String id);

    //INTERNAL FUNCTION
    Topic getTopicById(String id);
}
