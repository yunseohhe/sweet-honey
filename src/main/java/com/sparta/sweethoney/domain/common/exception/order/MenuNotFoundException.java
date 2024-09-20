package com.sparta.sweethoney.domain.common.exception.order;


import com.sparta.sweethoney.domain.common.exception.GlobalException;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.NO_STORE;

public class MenuNotFoundException extends GlobalException {
    public MenuNotFoundException() {
        super(NO_STORE);
    }
}
