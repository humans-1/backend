package org.example.humans.domain.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.humans.global.apiPayload.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserErrorCode implements BaseErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,
            "USER404_1", "사용자를 찾을 수 없습니다."),
    NO_USER_DATA_REGISTERED(HttpStatus.NOT_FOUND,
            "USER404_2", "사용자 데이터 값이 존재하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST,
            "USER400_1", "비밀번호가 다릅니다."),
    USER_INACTIVE(HttpStatus.FORBIDDEN,
            "USER403_1", "계정이 비활성화 상태입니다."),
    WRONG_AUTH_TYPE(HttpStatus.BAD_REQUEST,
            "USER400_2", "잘못된 인증 방식입니다.");




    private final HttpStatus status;
    private final String code;
    private final String message;
}
