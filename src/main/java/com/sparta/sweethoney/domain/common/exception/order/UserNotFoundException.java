package com.sparta.sweethoney.domain.common.exception.order;


import com.sparta.sweethoney.domain.common.exception.GlobalException;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.NO_USER;

public class UserNotFoundException extends GlobalException {
    public UserNotFoundException() {
        super(NO_USER);
    }
}
