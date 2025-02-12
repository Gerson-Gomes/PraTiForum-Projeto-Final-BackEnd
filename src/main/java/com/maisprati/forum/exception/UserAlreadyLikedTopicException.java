package com.maisprati.forum.exception;

public class UserAlreadyLikedTopicException extends RuntimeException {
    public UserAlreadyLikedTopicException(String message) {
        super(message);
    }
}
