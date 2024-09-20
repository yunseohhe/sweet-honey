package com.sparta.sweethoney.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GlobalExceptionConst {

    //Order_Exception
    NO_ORDERS_FOUND(HttpStatus.NO_CONTENT, " 주문 목록이 존재하지 않습니다."),

    // menu_Exception (상태코드 400)
    NOT_FOUND_MENU(HttpStatus.BAD_REQUEST, " 해당 메뉴가 존재하지 않습니다."),

    // menu_Exception (상태코드 409)
    ERR_PRODUCT_STOPPED(HttpStatus.CONFLICT, " 해당 메뉴는 이미 미판매 입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
