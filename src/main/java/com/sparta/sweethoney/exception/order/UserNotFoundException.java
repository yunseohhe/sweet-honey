package com.sparta.sweethoney.exception.order;

import com.sparta.sweethoney.exception.GlobalException;

import static com.sparta.sweethoney.exception.GlobalExceptionConst.NO_USER;

public class UserNotFoundException extends GlobalException {

    public UserNotFoundException() {
        super(NO_USER);
    }
}
