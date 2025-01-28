package com.mitchmele.feedback_listener;

import org.springframework.boot.SpringApplication;

public class TestFeedbackListenerApplication {

	public static void main(String[] args) {
		SpringApplication.from(FeedbackListenerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
