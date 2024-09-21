package com.sparta.sweethoney.domain.common.exception.store;

import com.sparta.sweethoney.domain.common.exception.GlobalException;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.MAX_STORE_LIMIT;

public class MaxStoreLimitException extends GlobalException {
    public MaxStoreLimitException() {
        super(MAX_STORE_LIMIT);
    }
}
