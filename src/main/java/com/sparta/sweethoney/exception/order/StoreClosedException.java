package com.sparta.sweethoney.exception.order;

import com.sparta.sweethoney.exception.GlobalException;

import static com.sparta.sweethoney.exception.GlobalExceptionConst.STORE_CLOSED;

public class StoreClosedException extends GlobalException {
    public StoreClosedException() {
        super(STORE_CLOSED);
    }
}
