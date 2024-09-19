package com.sparta.sweethoney.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GlobalExceptionConst {

    //Order_Exception
    NO_ORDERS_FOUND(HttpStatus.NOT_FOUND, " 주문 목록이 존재하지 않습니다."),
    NO_STORE(HttpStatus.NOT_FOUND, "가게가 존재하지 않습니다."),
    NO_MENU(HttpStatus.NOT_FOUND, "메뉴가 존재하지 않습니다."),
    NO_USER(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
