package com.sparta.sweethoney.domain.common.exception.review;

import com.sparta.sweethoney.domain.common.exception.GlobalException;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.*;

public class OrderNotCompleteException extends GlobalException {
    public OrderNotCompleteException() {
        super(ORDER_NOT_COMPLETE);
    }
}
