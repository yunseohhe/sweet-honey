package com.sparta.sweethoney.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {

    private final HttpStatus httpStatus;

    // 상수 -> 상태 코드랑 메시지가 담겨있다.
    public GlobalException(GlobalExceptionConst globalExceptionConst) {
        super(globalExceptionConst.name() + globalExceptionConst.getMessage());
        this.httpStatus = globalExceptionConst.getHttpStatus();
    }
}
