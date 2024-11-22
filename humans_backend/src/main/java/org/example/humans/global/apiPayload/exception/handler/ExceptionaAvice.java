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
    //애플리케이션에서 발생하는 커스텀 예외를 처리
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<CustomResponse<Void>> handleCustomException(GeneralException ex) {
        //예외가 발생하면 로그 기록
        log.warn("[ CustomException ]: {}", ex.getCode().getMessage());
        //커스텀 예외에 정의된 에러 코드와 메시지를 포함한 응답 제공
        return ResponseEntity.status(ex.getCode().getStatus())
                .body(ex.getCode().getErrorResponse());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<CustomResponse<String>> handleAllException(Exception ex) {
        log.error("[WARNING] Internal Server Error : {} ", ex.getMessage());
        BaseErrorCode errorCode = GeneralErrorCode.INTERNAL_SERVER_ERROR_500;
        CustomResponse<String> errorResponse = CustomResponse.onFailure(
                errorCode.getCode(),
                errorCode.getMessage(),
                ex.getMessage()
        );
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(errorResponse);
    }

}
