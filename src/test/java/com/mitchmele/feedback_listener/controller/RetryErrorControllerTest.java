package com.mitchmele.feedback_listener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchmele.feedback_listener.controller.advice.ErrorResponse;
import com.mitchmele.feedback_listener.controller.advice.RestControllerExceptionHandler;
import com.mitchmele.feedback_listener.model.ErrorEventPayload;
import com.mitchmele.feedback_listener.model.ProcessingResult;
import com.mitchmele.feedback_listener.model.ProcessingRetryException;
import com.mitchmele.feedback_listener.model.ProcessingStatus;
import com.mitchmele.feedback_listener.service.FeedbackRetryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RetryErrorControllerTest {

    private FeedbackRetryService mockRetryService;

    private RetryErrorController retryErrorController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private ErrorEventPayload errorEventPayload;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockRetryService = mock(FeedbackRetryService.class);
        retryErrorController = new RetryErrorController(mockRetryService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(retryErrorController)
                .setControllerAdvice(new RestControllerExceptionHandler())
                .build();

        errorEventPayload = ErrorEventPayload.builder()
                .email("some@email.com")
                .content("great site")
                .submissionTime("mockSubmissionTime")
                .feedbackId("feedbackId")
                .build();
    }

    @Test
    void shouldCallDbServiceWithErrorPayload() throws Exception {
        ProcessingResult expectedResult = ProcessingResult.builder().processingStatus(ProcessingStatus.SUCCESS).build();

        Mockito.when(mockRetryService.processAndSaveFeedback(any()))
                .thenReturn(new ResponseEntity<>(expectedResult, HttpStatus.CREATED));

        mockMvc.perform(post("/api/v1/error/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(errorEventPayload))
        ).andExpect(status().isCreated());

        Mockito.verify(mockRetryService).processAndSaveFeedback(errorEventPayload);
    }

    @Test
    void whenProcessingExceptionIsThrown_return500FromAdviceWithGenericMessage() throws Exception {

        when(mockRetryService.processAndSaveFeedback(any()))
                .thenThrow(new ProcessingRetryException("some error"));

        final ErrorResponse expectedRes = ErrorResponse.builder().errorMessage("retry attempt failed").build();

        mockMvc.perform(post("/api/v1/error/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(errorEventPayload))
        )
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedRes)));

    }
}