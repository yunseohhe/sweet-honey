package com.sparta.sweethoney.domain.common.exception.menu;

import com.sparta.sweethoney.domain.common.exception.GlobalException;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.ERR_PRODUCT_STOPPED;

public class ProductAlreadyStoppedException extends GlobalException {
    public ProductAlreadyStoppedException() {
        super(ERR_PRODUCT_STOPPED);
    }
}
