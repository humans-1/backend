package org.example.humans.global.apiPayload.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.humans.global.apiPayload.CustomResponse;
import org.example.humans.global.apiPayload.code.BaseErrorCode;
import org.example.humans.global.apiPayload.code.GeneralErrorCode;
import org.example.humans.global.apiPayload.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;


@Slf4j //로그 사용 가능
@RestControllerAdvice
public class ExceptionaAvice {
    @ExceptionHandler(GeneralException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomResponse<String>> customException(GeneralException e){
        BaseErrorCode code = e.getCode();

        //log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace())); //여러줄로 가져와서 찍음
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                CustomResponse.onFailure(code.getStatus(), code.getCode(), code.getMessage(), false, null));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<String>> exception(Exception e){
        BaseErrorCode code = GeneralErrorCode.INTERNAL_SERVER_ERROR_500;

        //log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace())); //여러줄로 가져와서 찍음

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                CustomResponse.onFailure(code.getStatus(),code.getCode(),code.getMessage(),false,null)
        );
    }

}
