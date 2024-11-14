package org.example.humans.global.jwt.exception;


import org.example.humans.global.apiPayload.exception.GeneralException;

public class AuthException extends GeneralException {

    public AuthException(JwtErrorCode code) {
        super(code);
    }
}
