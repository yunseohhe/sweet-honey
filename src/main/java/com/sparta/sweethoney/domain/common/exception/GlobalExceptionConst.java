package com.sparta.sweethoney.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GlobalExceptionConst {

    //Order_Exception
    NO_ORDER(HttpStatus.NOT_FOUND, " 주문이 존재하지 않습니다."),
    NO_STORE(HttpStatus.NOT_FOUND, "가게가 존재하지 않습니다."),
    NO_MENU(HttpStatus.NOT_FOUND, "메뉴가 존재하지 않습니다."),
    NO_USER(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다."),
    STORE_CLOSED(HttpStatus.FORBIDDEN, "가게 오픈 전입니다."),
    MINIMUM_ORDER_PRICE(HttpStatus.BAD_REQUEST, "주문 최소금액이 맞지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
