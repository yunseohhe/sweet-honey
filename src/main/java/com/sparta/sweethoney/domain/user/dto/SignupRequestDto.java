package com.sparta.sweethoney.domain.user.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String email;
    private String userName;
    private String password;
}
