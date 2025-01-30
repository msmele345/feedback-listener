package com.mitchmele.feedback_listener.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.feedback_listener.repository.FeedbackRepository;
import com.mitchmele.feedback_listener.model.FeedbackItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DbServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private ObjectMapper mockMapper;

    @InjectMocks
    private DbService dbService;

    @Test
    void whenMessageIsReceived_thenSaveToRepository() throws Exception {
        FeedbackItem feedbackItem = new FeedbackItem();
        feedbackItem.setFeedbackId("some-feedback-id");
        feedbackItem.setEmail("test@test.com");
        feedbackItem.setContent("Great website");
        feedbackItem.setSubmissionTime("mockTimestamp");

        when(mockMapper.readValue(anyString(), eq(FeedbackItem.class))).thenReturn(feedbackItem);

        dbService.saveMessage("mockPayload");

        Mockito.verify(feedbackRepository).save(feedbackItem);
    }
}