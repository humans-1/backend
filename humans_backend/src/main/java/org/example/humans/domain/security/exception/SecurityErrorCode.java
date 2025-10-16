package org.example.humans.domain.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.humans.global.apiPayload.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SecurityErrorCode implements BaseErrorCode {
    INVALID_TOKEN(HttpStatus.BAD_REQUEST,"SEC400_0","잘못된 토큰입니다."),
    BLACKLIST_TOKEN(HttpStatus.BAD_REQUEST,"SEC400_1", "블랙리스트로 등록된 토큰입니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"SEC401_0","인증되지 않은 회원입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,"SEC401_1","토큰이 만료되었습니다."),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED,"SEC401_2","잘못된 인증 정보입니다."),

    FORBIDDEN(HttpStatus.FORBIDDEN,"SEC403_0","잘못된 접근입니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"SEC404_0","존재하지 않는 계정입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND,"SEC404_1","refresh 토큰이 존재하지 않습니다."),

    INTERNAL_SECURITY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"SEC500_0","서버에서 인증처리 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
