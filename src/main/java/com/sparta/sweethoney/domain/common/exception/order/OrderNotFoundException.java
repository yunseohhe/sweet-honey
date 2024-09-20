package com.sparta.sweethoney.domain.common.exception.order;



import com.sparta.sweethoney.domain.common.exception.GlobalException;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.NO_ORDER;

public class OrderNotFoundException extends GlobalException {
    public OrderNotFoundException() {
        super(NO_ORDER);
    }
}
