package com.sparta.sweethoney.domain.common.exception.menu;

import com.sparta.sweethoney.domain.common.exception.GlobalException;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.NOT_FOUND_MENU;

public class NotFoundMenuException extends GlobalException {
    public NotFoundMenuException() {
        super(NOT_FOUND_MENU);
    }
}
