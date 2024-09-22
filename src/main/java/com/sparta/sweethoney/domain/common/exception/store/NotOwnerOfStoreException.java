package com.sparta.sweethoney.domain.common.exception.store;

import com.sparta.sweethoney.domain.common.exception.GlobalException;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.NOT_OWNER_OF_STORE;

public class NotOwnerOfStoreException extends GlobalException {
    public NotOwnerOfStoreException() {
        super(NOT_OWNER_OF_STORE);
    }
}
