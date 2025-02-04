package com.mitchmele.feedback_listener.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorEventPayload {

    private String feedbackId;
    private String content;
    private String email;
    private String submissionTime;
    private String errorTimestamp;
}
