package com.sparta.sweethoney.domain.common.exception.order;


import com.sparta.sweethoney.domain.common.exception.GlobalException;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.STORE_CLOSED;


public class StoreClosedException extends GlobalException {
    public StoreClosedException() {
        super(STORE_CLOSED);
    }
}
