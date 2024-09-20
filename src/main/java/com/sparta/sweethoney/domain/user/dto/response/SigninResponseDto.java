package com.sparta.sweethoney.domain.user.dto.response;

import lombok.Getter;

@Getter
public class SigninResponseDto {
    private String Token;

    public SigninResponseDto(String token){
        this.Token = token;
    }

}
