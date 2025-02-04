package com.mitchmele.feedback_listener.integration;

import com.mitchmele.feedback_listener.controller.advice.ErrorResponse;
import com.mitchmele.feedback_listener.model.ErrorEventPayload;
import com.mitchmele.feedback_listener.model.FeedbackItem;
import com.mitchmele.feedback_listener.model.ProcessingResult;
import com.mitchmele.feedback_listener.model.ProcessingStatus;
import com.mitchmele.feedback_listener.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


@Tag("integrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RetryIntegrationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @MockBean
    private FeedbackRepository feedbackRepository;

    ErrorEventPayload inboundPayload;

    @BeforeEach
    void setUp() {
        inboundPayload = ErrorEventPayload.builder()
                .email("some@email.com")
                .content("great site")
                .submissionTime("mockSubmissionTime")
                .feedbackId("feedbackId")
                .build();
    }

    @Test
    void retryEndToEnd() {
        FeedbackItem feedbackItem = new FeedbackItem();
        feedbackItem.setFeedbackId("feedbackId");
        feedbackItem.setEmail("some@email.com");
        feedbackItem.setContent("great site");
        feedbackItem.setSubmissionTime("mockSubmissionTime");

        Mockito.when(feedbackRepository.save(Mockito.any())).thenReturn(feedbackItem);

        final ResponseEntity<ProcessingResult> actualResponse = testRestTemplate
                .postForEntity(createUrlWithPort("/api/v1/retry/feedback"), inboundPayload, ProcessingResult.class);

        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actualResponse.getBody()).isNotNull();
        assertThat(actualResponse.getBody().getProcessingStatus()).isEqualTo(ProcessingStatus.SUCCESS);

        ArgumentCaptor<FeedbackItem> captor = ArgumentCaptor.forClass(FeedbackItem.class);

        Mockito.verify(feedbackRepository).save(captor.capture());
        assertThat(captor.getValue().getFeedbackId()).isEqualTo("feedbackId");
    }

    @Test
    void whenDbSaveFails_throwProcessingRetryExceptionAndReturn500() {

        Mockito.when(feedbackRepository.save(Mockito.any()))
                .thenThrow(new RuntimeException("db error"));

        final ResponseEntity<ErrorResponse> actual = testRestTemplate.postForEntity(
                createUrlWithPort("/api/v1/retry/feedback"),
                inboundPayload,
                ErrorResponse.class
        );

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(actual.getBody()).isNotNull();
        assertThat(actual.getBody().getErrorMessage()).isEqualTo("retry attempt failed");
    }

    private String createUrlWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
