package org.example.humans.global.apiPayload.exception;

import lombok.Getter;
import org.example.humans.global.apiPayload.code.BaseErrorCode;

@Getter
public class GeneralException extends RuntimeException{

    private final BaseErrorCode code;

    public GeneralException(BaseErrorCode code) {
        this.code = code;
    }
}
