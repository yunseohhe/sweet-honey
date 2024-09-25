package com.sparta.sweethoney.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    GUEST("일반 사용자"),   // 일반 사용자
    OWNER("가게 영업자"),  // 사장
    PENDING("대기 사용자");    // 대기 상태

    private String message;
}
