package com.mitchmele.feedback_listener.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.feedback_listener.FeedbackRepository;
import com.mitchmele.feedback_listener.model.FeedbackItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbService {

    private final ObjectMapper objectMapper;
    private final FeedbackRepository feedbackRepository;

    public void saveMessage(String message) {
        try {
            final FeedbackItem feedbackItem = objectMapper.readValue(message, FeedbackItem.class);
            feedbackRepository.save(feedbackItem);
            log.info("Feedback saved for Id: {}", feedbackItem.getFeedbackId());
        } catch (Exception e) {
            log.info("Error while saving feedback: {}", e.getMessage());
        }
    }
}
