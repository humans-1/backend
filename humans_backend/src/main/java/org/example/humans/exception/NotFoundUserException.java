package org.example.humans.exception;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(String msg) {
        super(msg);
    }
}
