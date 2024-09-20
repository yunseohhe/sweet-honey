package com.sparta.sweethoney.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {

    private final HttpStatus httpStatus;

    public GlobalException(GlobalExceptionConst globalExceptionConst) {
        super(globalExceptionConst.name() + globalExceptionConst.getMessage());
        this.httpStatus = globalExceptionConst.getHttpStatus();
    }
}
