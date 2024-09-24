package com.sparta.sweethoney.domain.common.exception.user;


import com.sparta.sweethoney.domain.common.exception.GlobalException;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.NOT_FOUND_USERROLE;

public class NotFoundUserRoleException extends GlobalException {
    public NotFoundUserRoleException() {
        super(NOT_FOUND_USERROLE);
    }
}
