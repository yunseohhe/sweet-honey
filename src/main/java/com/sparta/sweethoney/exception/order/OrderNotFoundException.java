package com.sparta.sweethoney.exception.order;

import com.sparta.sweethoney.exception.GlobalException;

import static com.sparta.sweethoney.exception.GlobalExceptionConst.NO_ORDERS_FOUND;

public class OrderNotFoundException extends GlobalException {

    public OrderNotFoundException() {
        super(NO_ORDERS_FOUND);
    }
}
