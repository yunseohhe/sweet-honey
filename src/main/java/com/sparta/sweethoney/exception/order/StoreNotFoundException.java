package com.sparta.sweethoney.exception.order;

import com.sparta.sweethoney.exception.GlobalException;

import static com.sparta.sweethoney.exception.GlobalExceptionConst.NO_STORE;

public class StoreNotFoundException extends GlobalException {

    public StoreNotFoundException() {
        super(NO_STORE);
    }
}
