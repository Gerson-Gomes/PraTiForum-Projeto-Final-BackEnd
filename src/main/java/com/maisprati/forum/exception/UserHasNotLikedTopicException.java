package com.maisprati.forum.exception;

public class UserHasNotLikedTopicException extends RuntimeException{
    public UserHasNotLikedTopicException(String message) {
        super(message);
    }
}
