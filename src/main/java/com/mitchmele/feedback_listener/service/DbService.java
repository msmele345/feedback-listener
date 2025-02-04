package com.mitchmele.feedback_listener.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.feedback_listener.model.ErrorEventPayload;
import com.mitchmele.feedback_listener.model.ProcessingResult;
import com.mitchmele.feedback_listener.model.ProcessingStatus;
import com.mitchmele.feedback_listener.repository.FeedbackRepository;
import com.mitchmele.feedback_listener.model.FeedbackItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.mitchmele.feedback_listener.util.AppLogger.logFailure;
import static com.mitchmele.feedback_listener.util.AppLogger.logSuccess;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbService {

    private final ObjectMapper objectMapper;
    private final FeedbackRepository feedbackRepository;

    public ProcessingResult saveMessage(String message) {
        try {
            final FeedbackItem feedbackItem = objectMapper.readValue(message, FeedbackItem.class);
            final FeedbackItem savedItem = feedbackRepository.save(feedbackItem);

            logSuccess(savedItem.getFeedbackId());

            return new ProcessingResult(ProcessingStatus.SUCCESS);
        } catch (Exception e) {
            log.info("error while saving feedback: {}", e.getMessage());

            return new ProcessingResult(ProcessingStatus.FAILURE);
        }
    }

    public ProcessingResult saveRetryMessage(ErrorEventPayload errorEventPayload) {
        FeedbackItem feedbackItem = new FeedbackItem();
        feedbackItem.setFeedbackId(errorEventPayload.getFeedbackId());
        feedbackItem.setEmail(errorEventPayload.getEmail());
        feedbackItem.setContent(errorEventPayload.getEmail());
        feedbackItem.setSubmissionTime(errorEventPayload.getSubmissionTime());
        feedbackItem.setErrorRetry(true);

        try {
            final FeedbackItem savedItem = feedbackRepository.save(feedbackItem);
            logSuccess(savedItem.getFeedbackId());
            return new ProcessingResult(ProcessingStatus.SUCCESS);
        } catch (Exception e) {
            logFailure(e.getMessage());
        }

        return new ProcessingResult(ProcessingStatus.FAILURE);
    }
}
