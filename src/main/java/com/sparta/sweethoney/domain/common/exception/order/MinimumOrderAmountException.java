package com.sparta.sweethoney.domain.common.exception.order;

import com.sparta.sweethoney.domain.common.exception.GlobalException;

import static com.sparta.sweethoney.domain.common.exception.GlobalExceptionConst.MINIMUM_ORDER_PRICE;

public class MinimumOrderAmountException extends GlobalException {
    //MinimumOrderAmountException
    public MinimumOrderAmountException() {
        super(MINIMUM_ORDER_PRICE);
    }
}
