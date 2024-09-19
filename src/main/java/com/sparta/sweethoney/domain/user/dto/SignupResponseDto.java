package com.sparta.sweethoney.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponseDto {

    private String Token;

    public SignupResponseDto(String token){
        this.Token = token;
    }

}
