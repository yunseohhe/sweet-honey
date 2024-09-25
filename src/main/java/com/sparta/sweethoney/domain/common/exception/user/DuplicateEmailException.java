package com.sparta.sweethoney.domain.common.exception.user;

import com.sparta.sweethoney.domain.common.exception.GlobalException;
import com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.DUPLICATE_EMAIL;


public class DuplicateEmailException extends GlobalException {
    public DuplicateEmailException() {
        super(DUPLICATE_EMAIL);
    }
}
