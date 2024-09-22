package com.sparta.sweethoney.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GlobalExceptionConst {
    //order_Exception (상태코드 400)
    MINIMUM_ORDER_PRICE(HttpStatus.BAD_REQUEST, " 주문 최소금액이 맞지 않습니다."),

    //order_Exception (상태코드 403),
    STORE_CLOSED(HttpStatus.FORBIDDEN, " 가게 오픈 전입니다."),
    UNAUTHORIZED_USER(HttpStatus.FORBIDDEN, " 사용자 권한이 없습니다."),

    //order_Exception (상태코드 404)
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, " 주문이 존재하지 않습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, " 회원이 존재하지 않습니다."),

    // menu_Exception (상태코드 400)
    NOT_FOUND_MENU(HttpStatus.BAD_REQUEST, " 해당 메뉴가 존재하지 않습니다."),

    // menu_Exception (상태코드 409)
    ERR_PRODUCT_STOPPED(HttpStatus.CONFLICT, " 해당 메뉴는 이미 미판매 입니다."),

    // store_Exception (상태코드 404)
    NOT_FOUND_STORE(HttpStatus.NOT_FOUND, " 가게를 찾을 수 없습니다."),

    // store_Exception (상태코드 403)
    NOT_OWNER_OF_STORE(HttpStatus.FORBIDDEN, " 해당 가게의 소유자가 아닙니다."),

    // store_Exception (상태코드 409)
    MAX_STORE_LIMIT(HttpStatus.CONFLICT, " 사장님은 최대 3개의 가게만 운영할 수 있습니다."),

    // Review_Exception (상태코드 400)
    INVALID_RATING(HttpStatus.BAD_REQUEST, "평점은 1점에서 5점 사이여야 합니다."),

    // Review_Exception (상태코드 403)
    ORDER_NOT_COMPLETE(HttpStatus.FORBIDDEN, "배달 완료된 주문에만 리뷰를 작성할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
