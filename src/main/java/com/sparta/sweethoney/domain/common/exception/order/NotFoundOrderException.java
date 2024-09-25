package com.sparta.sweethoney.domain.common.exception.order;

import com.sparta.sweethoney.domain.common.exception.GlobalException;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.NOT_FOUND_ORDER;

public class NotFoundOrderException extends GlobalException {
    public NotFoundOrderException() {
        super(NOT_FOUND_ORDER);
    }
}
