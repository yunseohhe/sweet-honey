package com.sparta.sweethoney.domain.common.dto;

import lombok.Getter;

@Getter
public class AuthUser {
    private Long id;

    public AuthUser(Long id) {
        this.id = id;
    }
}
