package org.example.humans.domain.note.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.humans.global.apiPayload.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum NoteErrorCode implements BaseErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,
            "NOTE404", "과목을 찾을 수 없습니다."),
;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
