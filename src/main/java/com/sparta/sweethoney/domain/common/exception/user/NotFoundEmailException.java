package com.sparta.sweethoney.domain.common.exception.user;

import com.sparta.sweethoney.domain.common.exception.GlobalException;
import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.NOT_FOUND_EMAIL;

public class NotFoundEmailException extends GlobalException {
    public NotFoundEmailException() {
        super(NOT_FOUND_EMAIL);
    }
}
