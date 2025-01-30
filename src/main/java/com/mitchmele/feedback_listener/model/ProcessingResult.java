package com.mitchmele.feedback_listener.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessingResult {

    private ProcessingStatus processingStatus;
}
