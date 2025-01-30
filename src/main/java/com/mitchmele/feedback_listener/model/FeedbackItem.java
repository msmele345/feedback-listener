package com.mitchmele.feedback_listener.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Container(containerName = "Feedback")
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackItem {

    @Id
    private String feedbackId;

    private String content;

    @PartitionKey
    private String email;

    private String submissionTime;
}
