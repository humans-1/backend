package org.example.humans.domain.user.exception;

import org.example.humans.global.apiPayload.code.BaseErrorCode;
import org.example.humans.global.apiPayload.exception.GeneralException;

public class UserException extends GeneralException {
    public UserException(BaseErrorCode code){
        super(code);
    }
}
