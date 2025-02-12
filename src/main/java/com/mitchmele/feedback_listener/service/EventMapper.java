package com.mitchmele.feedback_listener.service;

public interface EventMapper<T, R> {

    R mapFrom(T event);
}
