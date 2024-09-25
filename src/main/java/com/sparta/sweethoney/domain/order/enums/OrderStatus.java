package com.sparta.sweethoney.domain.order.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus {
    PENDING("주문 대기중"),
    ACCEPTED("주문 접수 완료"),
    COOKING("메뉴 준비중"),
    DELIVERING("배달중"),
    COMPLETE("배달 완료"),
    CANCEL("주문 취소");

    private String description;
}
