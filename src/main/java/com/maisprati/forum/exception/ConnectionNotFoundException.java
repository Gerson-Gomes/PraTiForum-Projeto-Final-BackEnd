package com.maisprati.forum.exception;


public class ConnectionNotFoundException extends RuntimeException {
    public ConnectionNotFoundException(String message) {
        super(message);
    }
}
