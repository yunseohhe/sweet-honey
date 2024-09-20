package com.sparta.sweethoney.domain.common.exception.orderexception;

import com.sparta.sweethoney.domain.common.exception.GlobalException;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.NO_ORDERS_FOUND;

public class NoOrdersFoundException extends GlobalException {

    public NoOrdersFoundException() {
        super(NO_ORDERS_FOUND);
    }
}
