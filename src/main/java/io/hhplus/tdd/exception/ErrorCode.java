package io.hhplus.tdd.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //400
    ILLEGAL_ID(BAD_REQUEST, "유효하지 않은 ID 입니다."),
    //409
    NOT_ENOUGH_POINTS(CONFLICT, "포인트가 충분하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
