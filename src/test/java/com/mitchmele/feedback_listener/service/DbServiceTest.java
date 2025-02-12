package com.mitchmele.feedback_listener.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.feedback_listener.model.ErrorEventPayload;
import com.mitchmele.feedback_listener.model.ProcessingResult;
import com.mitchmele.feedback_listener.model.ProcessingStatus;
import com.mitchmele.feedback_listener.repository.FeedbackRepository;
import com.mitchmele.feedback_listener.model.FeedbackItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DbServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private ObjectMapper mockMapper;

    @Mock
    private RetryToFeedbackItemMapper mockFeedbackItemMapper;

    @InjectMocks
    private DbService dbService;

    private FeedbackItem feedbackItem;

    @BeforeEach
    void setUp() {
        feedbackItem = new FeedbackItem();
        feedbackItem.setFeedbackId("some-feedback-id");
        feedbackItem.setEmail("test@test.com");
        feedbackItem.setContent("Great website");
        feedbackItem.setSubmissionTime("mockTimestamp");
    }

    @Test
    void whenMessageIsReceived_thenSaveToRepository() throws Exception {
        when(mockMapper.readValue(anyString(), eq(FeedbackItem.class))).thenReturn(feedbackItem);

        when(feedbackRepository.save(any())).thenReturn(feedbackItem);

        final ProcessingResult result = dbService.saveMessage("mockPayload");

        assertThat(result.getProcessingStatus()).isEqualTo(ProcessingStatus.SUCCESS);
        Mockito.verify(feedbackRepository).save(feedbackItem);
    }

    @Test
    void whenCosmosSaveHasError_thenDoNotThrowAndReturnProcessingResultOfFailure() throws Exception {
        when(mockMapper.readValue(anyString(), eq(FeedbackItem.class))).thenReturn(feedbackItem);

        when(feedbackRepository.save(feedbackItem)).thenThrow(new RuntimeException("save error"));

        final ProcessingResult result = dbService.saveMessage("mockPayload");

        assertThat(result.getProcessingStatus()).isEqualTo(ProcessingStatus.FAILURE);
    }

    @Test
    void saveRetryMessage_mapErrorPayloadToFeedbackItemWithErrorRetryTrue() {
        final ErrorEventPayload errorEventPayload = ErrorEventPayload.builder()
                .email("some@email.com")
                .content("great site")
                .submissionTime("mockSubmissionTime")
                .feedbackId("feedbackId")
                .build();

        final FeedbackItem feedbackItemFromError = new FeedbackItem();
        feedbackItemFromError.setFeedbackId("some-feedback-id");
        feedbackItemFromError.setEmail("test@test.com");
        feedbackItemFromError.setContent("Great website");
        feedbackItemFromError.setErrorRetry(true);
        feedbackItemFromError.setSubmissionTime("mockTimestamp");

        when(mockFeedbackItemMapper.mapFrom(errorEventPayload)).thenReturn(feedbackItemFromError);

        final ProcessingResult result = dbService.saveRetryMessage(errorEventPayload);

        Mockito.verify(feedbackRepository).save(feedbackItemFromError);
        Mockito.verify(mockFeedbackItemMapper).mapFrom(errorEventPayload);

        assertThat(result.getProcessingStatus()).isEqualTo(ProcessingStatus.FAILURE);
    }
}