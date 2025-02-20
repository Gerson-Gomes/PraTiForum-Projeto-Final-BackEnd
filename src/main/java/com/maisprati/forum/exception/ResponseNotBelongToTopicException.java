package com.maisprati.forum.exception;

public class ResponseNotBelongToTopicException extends RuntimeException {
    public ResponseNotBelongToTopicException(String message) {
        super(message);
    }
}
