package com.sparta.sweethoney.domain.common.exception.order;

import com.sparta.sweethoney.domain.common.exception.GlobalException;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.UNAUTHORIZED_USER;

public class UnauthorizedAccessException extends GlobalException {
    public UnauthorizedAccessException() {
        super(UNAUTHORIZED_USER);
    }
}
