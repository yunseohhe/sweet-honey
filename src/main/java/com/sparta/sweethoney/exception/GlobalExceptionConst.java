package com.sparta.sweethoney.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GlobalExceptionConst {

    //Order_Exception
    NO_ORDERS_FOUND(HttpStatus.NO_CONTENT, " 주문 목록이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
