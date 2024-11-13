package org.example.humans.global.apiPayload.code;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {
    HttpStatus getStatus();
    String getCode();
    String getMessage();
}
