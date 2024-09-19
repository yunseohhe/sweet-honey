package com.sparta.sweethoney.exception.orderexception;

import com.sparta.sweethoney.exception.GlobalException;

import static com.sparta.sweethoney.exception.GlobalExceptionConst.NO_ORDERS_FOUND;

public class NoOrdersFoundException extends GlobalException {

    public NoOrdersFoundException() {
        super(NO_ORDERS_FOUND);
    }
}
