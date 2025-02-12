package com.mitchmele.feedback_listener.service;

import com.mitchmele.feedback_listener.model.ErrorEventPayload;
import com.mitchmele.feedback_listener.model.FeedbackItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class RetryToFeedbackItemMapperTest {

    private RetryToFeedbackItemMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RetryToFeedbackItemMapper();
    }

    @Test
    void shouldTransformErrorPayloadToDomainObj() {
       final ErrorEventPayload errorEventPayload = ErrorEventPayload.builder()
                .email("some@email.com")
                .content("great site")
                .submissionTime("mockSubmissionTime")
                .feedbackId("feedbackId")
                .build();

        FeedbackItem expected = new FeedbackItem();
        expected.setFeedbackId(errorEventPayload.getFeedbackId());
        expected.setEmail(errorEventPayload.getEmail());
        expected.setContent(errorEventPayload.getEmail());
        expected.setSubmissionTime(errorEventPayload.getSubmissionTime());
        expected.setErrorRetry(true);

        final FeedbackItem actual = mapper.mapFrom(errorEventPayload);

        assertThat(actual).isEqualTo(expected);
    }
}