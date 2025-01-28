package com.mitchmele.feedback_listener;

import com.azure.spring.messaging.implementation.annotation.EnableAzureMessaging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAzureMessaging
@SpringBootApplication
public class FeedbackListenerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedbackListenerApplication.class, args);
	}

}
