package com.sparta.sweethoney.domain.common.exception.order;

import com.sparta.sweethoney.domain.common.exception.GlobalException;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.NOT_FOUND_STORE;

public class NotFoundStoreException extends GlobalException {
    public NotFoundStoreException() {
        super(NOT_FOUND_STORE);
    }
}
